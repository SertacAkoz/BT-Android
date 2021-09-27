package com.example.bluetoothproject;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    BluetoothAdapter myBluetooth;
    BluetoothDevice mmDevice;
    BluetoothSocket mmSocket;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
   // boolean stopWorker;

    //Buttons
    RelativeLayout btnOpenBluetooth;
    RelativeLayout btnCloseBluetooth;
    RelativeLayout btnSendData;

    //EditTexts
    EditText edtTextValfGecisSuresi;
    EditText edtTextReferansBasinc;
    EditText edtTextValfCalismaSuresi;
    EditText edtTextBeklemeSuresi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        btnOpenBluetooth =(RelativeLayout) findViewById(R.id.btnOpenBluetooth);
        btnCloseBluetooth =(RelativeLayout) findViewById(R.id.btnCloseBluetooth);
        btnSendData =(RelativeLayout) findViewById(R.id.btnSendData);

        edtTextValfGecisSuresi = (EditText) findViewById(R.id.edtTextValfGecisSuresi);
        edtTextReferansBasinc = (EditText) findViewById(R.id.edtTextReferansBasinc);
        edtTextValfCalismaSuresi = (EditText) findViewById(R.id.edtTextValfCalismaSuresi);
        edtTextBeklemeSuresi = (EditText) findViewById(R.id.edtTextBeklemeSuresi);

        btnOpenBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    findBT();
                    openBT();
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnCloseBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    closeBT();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sendData();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    void findBT() {
        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        if(myBluetooth == null)
        {
           // myLabel.setText("No bluetooth adapter available");
            Toast.makeText(getApplicationContext(), "No bluetooth adapter available", Toast.LENGTH_SHORT).show();
        }

        if(!myBluetooth.isEnabled())
        {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }

        Set<BluetoothDevice> pairedDevices = myBluetooth.getBondedDevices();
        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice device : pairedDevices)
            {
                if(device.getName().equals("HC05"))
                {
                    mmDevice = device;
                    break;
                }
            }
        }
        //myLabel.setText("Bluetooth Device Found");
        Toast.makeText(getApplicationContext(), "Bluetooth Device Found", Toast.LENGTH_SHORT).show();
    }
    void openBT() throws IOException {
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
        mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
        mmSocket.connect();
        mmOutputStream = mmSocket.getOutputStream();
        mmInputStream = mmSocket.getInputStream();
        //myLabel.setText("Bluetooth Opened");
        Toast.makeText(getApplicationContext(), "Bluetooth Opened", Toast.LENGTH_SHORT).show();
        //beginListenForData();
    }
//    void beginListenForData() {
//        final Handler handler = new Handler();
//        final byte delimiter = 10; //This is the ASCII code for a newline character
//
//        stopWorker = false;
//        final int[] readBufferPosition = {0};
//        byte[] readBuffer = new byte[1024];
//        Thread workerThread = new Thread(new Runnable()
//        {
//            public void run()
//            {
//                while(!Thread.currentThread().isInterrupted() && !stopWorker)
//                {
//                    try
//                    {
//                        int bytesAvailable = mmInputStream.available();
//                        if(bytesAvailable > 0)
//                        {
//                            byte[] packetBytes = new byte[bytesAvailable];
//                            mmInputStream.read(packetBytes);
//                            for(int i=0;i<bytesAvailable;i++)
//                            {
//                                byte b = packetBytes[i];
//                                if(b == delimiter)
//                                {
//                                    byte[] encodedBytes = new byte[readBufferPosition[0]];
//                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
//                                    final String data = new String(encodedBytes, "US-ASCII");
//                                    readBufferPosition[0] = 0;
//
//                                    handler.post(new Runnable()
//                                    {
//                                        public void run()
//                                        {
//                                           // myLabel.setText(data);
//                                            Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                                }
//                                else
//                                {
//                                    readBuffer[readBufferPosition[0]++] = b;
//                                }
//                            }
//                        }
//                    }
//                    catch (IOException ex)
//                    {
//                        stopWorker = true;
//                    }
//                }
//            }
//        });
//
//        workerThread.start();
//    }

    void sendData() throws IOException {
        //String msg = myTextbox.getText().toString();

        try {
            if (edtTextBeklemeSuresi.getText().toString().equals("") || edtTextValfCalismaSuresi.getText().toString().equals("") || edtTextValfGecisSuresi.getText().toString().equals("") || edtTextReferansBasinc.getText().toString().equals("") ){
                Toast.makeText(getApplicationContext(), "Lütfen belirtilen boş bırakmayınız", Toast.LENGTH_SHORT).show();
            }else{
                String myData = edtTextValfGecisSuresi.getText().toString()+":"+edtTextReferansBasinc.getText().toString()+":"+edtTextValfCalismaSuresi.getText().toString()+":"+edtTextBeklemeSuresi.getText().toString()+"@";
                //myData += "\n";
                mmOutputStream.write(myData.getBytes());
                //myLabel.setText("Data Sent");
                Toast.makeText(getApplicationContext(), "Data Sent", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }





    }
    void closeBT() throws IOException {
        try {
           // stopWorker = true;
            mmOutputStream.close();
            //mmInputStream.close();
            mmSocket.close();
            //myLabel.setText("Bluetooth Closed");
            Toast.makeText(getApplicationContext(), "Bluetooth Closed", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}