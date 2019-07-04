package com.example.hp.androidcommunication;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    EditText e1 ,e2;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        e1=findViewById(R.id.e1);
        e2=findViewById(R.id.e2);
        btn=findViewById(R.id.btn);
        Thread mythread=new Thread(new MyServer());
        mythread.start();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackgroundTask b=new BackgroundTask();
                b.execute(e1.getText().toString(),e2.getText().toString());
            }
        });

    }

    class MyServer implements Runnable
    {
        ServerSocket ss;
        Socket mySocket;
        DataInputStream dis;
        String message;
        Handler handler=new Handler();
        @Override
        public void run() {
            try
            {
                ss=new ServerSocket((9700));
                while (true)
                {
                    mySocket =ss.accept();
                    dis=new DataInputStream(mySocket.getInputStream());
                    message=dis.readUTF();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "message received from other client: "+message, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }


        }
    }

    class BackgroundTask extends AsyncTask<String ,Void,String>
    {
        Socket s;
        DataOutputStream dos;
        String ip,message;
        @Override
        protected String doInBackground(String... params) {
            ip = params[0];
            message = params[1];
            try {
                s = new Socket(ip,9700);
                dos = new DataOutputStream(s.getOutputStream());
                dos.writeUTF(message);

                dos.close();

                s.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return null;
        }
    }
}
