package com.example.thmonitor;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.thmonitor.db.Temperature;
import com.example.thmonitor.socket.SocketTransceiver;
import com.example.thmonitor.socket.TcpClient;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

public class IPConfig extends AppCompatActivity implements OnClickListener {

    private EditText ipEditText;
    private EditText portEditText;
    private Button connectButton;
    private Button visitButton;

    private Handler handler = new Handler(Looper.getMainLooper());

    private TcpClient client = new TcpClient() {
        @Override
        public void onConnect(SocketTransceiver transceiver) {
            Connector.getDatabase();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(IPConfig.this, "Connect", Toast.LENGTH_SHORT).show();
                    refreshUI(false);
                    send();
                    Intent intent = new Intent(IPConfig.this, PrimaryActivity.class);
                    intent.putExtra("visit", false);
                    startActivity(intent);
                    IPConfig.this.finish();
                }
            });
        }

        @Override
        public void onConnectFailed() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(IPConfig.this, "ConnectFailed", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onReceive(SocketTransceiver transceiver, String s) {

            String[] words = s.split("[/: ]");
            String whereStr = "year == ? and month == ? and day == ? and hour == ? and minute == ?";
            List<Temperature> temperatures = DataSupport.where(whereStr, words[0], words[1], words[2], words[3], words[4])
                    .find(Temperature.class);
//            List<Temperature> temperatures = DataSupport.findAll(Temperature.class);
            if(temperatures.size() == 0) {
                Temperature temp = new Temperature();
                temp.setYear(words[0]);
                temp.setMonth(words[1]);
                temp.setDay(words[2]);
                temp.setHour(words[3]);
                temp.setMinute(words[4]);
                temp.setTemp(Float.parseFloat(words[5]));
                float timeFlag = (Float.parseFloat(words[0]) - 2017) * 535680 +
                        (Float.parseFloat(words[1]) - 1) * 44640 +
                        (Float.parseFloat(words[2]) - 1) * 1440;
                temp.setTimeFlag(String.valueOf(timeFlag));
                temp.save();
            }

        }

        @Override
        public void onDisconnect(SocketTransceiver transceiver) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(IPConfig.this, "Disconnect", Toast.LENGTH_SHORT).show();
                    refreshUI(true);
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipconfig);

        ipEditText = (EditText)findViewById(R.id.ip_edit_text);
        portEditText = (EditText)findViewById(R.id.port_edit_text);
        connectButton = (Button)findViewById(R.id.connect_btn);
        visitButton = (Button)findViewById(R.id.visit_btn);

        connectButton.setOnClickListener(this);
        visitButton.setOnClickListener(this);

        refreshUI(true);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.connect_btn:
                connect();
//                if(client.isConnected())
//                    send();
                break;
            case R.id.visit_btn:
                Intent intent = new Intent(IPConfig.this, PrimaryActivity.class);
                intent.putExtra("visit", true);
                startActivity(intent);
                IPConfig.this.finish();
                break;
        }
    }

    private void connect() {
        if(client.isConnected()) {
            client.disconnect();
        } else {
            try {
                String hostIP = ipEditText.getText().toString();
                int port = Integer.parseInt(portEditText.getText().toString());
                client.connect(hostIP, port);
            } catch(NumberFormatException e) {
                Toast.makeText(IPConfig.this, "输入不合法", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    private void send() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                client.getTransceiver().send("get_data");
            }
        }).start();
    }

    private void refreshUI(final boolean flag) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                ipEditText.setEnabled(flag);
                portEditText.setEnabled(flag);
                connectButton.setText(flag ? "Connect" : "Disconnect");
            }
        });
    }

}
