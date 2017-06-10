package com.purefaithstudio.gurbani;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

public class Mp3PlayerService extends Service {

    public static MediaPlayer player;
    public static boolean oncomplete,isprepared=false;
    private boolean isPlayed;
    private ToggleListener toggleListener;
    private int callType;

    public Mp3PlayerService() {
    }

    public void setToggleListener(ToggleListener toggleListener) {
        this.toggleListener = toggleListener;
    }

    public void init(String url) throws Exception {
        player = new MediaPlayer();
        try {
            player.setDataSource(url);
            Log.i("RecordShow", "datasource " + url);
            // player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        Log.i("Playercheck", "init completed");
        //play audio
        player.prepareAsync();
        player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.i("Playercheck", "Player errors-" + what + extra);
                //Toast.makeText(getApplicationContext(),"Unable to connect Try again",Toast.LENGTH_LONG);
                player.stop();
                player.release();
                isprepared=false;
                player = null;
                return true;
            }
        });
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.i("Playercheck", "mp.prepared");
               // Toast.makeText(getApplicationContext(), "Ready To Play", Toast.LENGTH_SHORT);
                if (callType == 0) {
                    mp.start();
                    isprepared=true;
                    send(true);
                }
            }
        });
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                player.stop();
                player.release();
                isprepared=false;
                player = null;
                Log.i("Playercheck","stop complete");
               // Toast.makeText(getApplicationContext(), "finished", Toast.LENGTH_LONG);
                send(false);
                oncomplete = true;
                Mp3PlayerService.this.stopSelf();
            }
        });
        player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                if (isprepared) {
                    long duration = mp.getDuration();
                    duration = 100 / ((duration / 1000) / 60);
                    Log.i("buffer", "buffering.." + percent + "  " + duration);
                    if (percent > duration && duration != 0) {
                        if (!player.isPlaying() && !isPlayed) {
                            player.start();
                            isPlayed = true;
                            Log.i("Playercheck", "mp.started");
                            //Toast.makeText(getApplicationContext(), "Playing", Toast.LENGTH_LONG);
                            send(true);
                        }
                    }
                }
                send(true);
            }
        });

    }

    private void send(Boolean run) {
        Intent intent = new Intent("com.purefaithstudio.gurbani");
        intent.putExtra("run",run);
        intent.setAction("com.purefaithstudio.gurbani.Mp3Player");
        sendBroadcast(intent);
    }

    /*private String getUrl(int position) throws NullPointerException {
        ArrayList<Upload.File> files = MainActivity.apm.getFileArrayList();
        String url = "";
        for (Upload.File file : files) {
            if (names[position].equals(file.getName())) {
                url = file.getUrl();
                Log.i("Playercheck", "Url founded pos:" + position + "  " + url);
                break;
            }
            Log.i("Playercheck", "Advance for");
        }
        return url;
    }*/


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String url = intent.getExtras().getString("url");
            try {
                callType = intent.getExtras().getInt("type");
                init(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
            /*int position = intent.getExtras().getInt("key");
            try {
                init(position);
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        } else {
            System.out.println("Intent Empty destroying self");
            this.stopSelf();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (player != null) {
                if (player.isPlaying()) {
                    player.stop();
                    player.release();
                    isprepared=false;
                    player = null;
                    Log.i("Playercheck", "Service OnDestroy:" + Thread.currentThread().getId());
                }
            }

        } catch (Exception e) {
            System.out.println("Cannot destroy MP3Service");
            e.printStackTrace();
        }
    }

    public interface ToggleListener {
        public void check();
    }
}