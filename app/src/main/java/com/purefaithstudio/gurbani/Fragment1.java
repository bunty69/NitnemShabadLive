package com.purefaithstudio.gurbani;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.shephertz.app42.paas.sdk.android.upload.Upload;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by MY System on 4/1/2015.
 */
public class Fragment1 extends Fragment implements MyArrayAdapter.ClickListener, AudioManager.OnAudioFocusChangeListener {
    public static int height;
    public Intent intent;
    String largeText;
    String pathText;
    private Intent i;
    private Bundle b;
    private int title;
    private Display display;
    private Information[] itemdata = {new Information("cOpeI swihb", R.drawable.khanda),
            new Information("suKmnI swihb", R.drawable.khanda),
            new Information("jpujI swihb", R.drawable.khanda),
            new Information("rhrwis swihb", R.drawable.khanda),
            new Information("AnMdU swihb", R.drawable.khanda),
            new Information("jwpu swihb", R.drawable.khanda),
            new Information("Awsw dI vwr", R.drawable.khanda),
            new Information("qÍ pRswid sv`Xy", R.drawable.khanda)};
    private boolean togglePlay=false;
    private int currentPosition = -3;
    private View currentView;
    private boolean serviceStarted;
    private String[] names = {"chaupaisahib", "sukhmanisahib", "japjisahib", "rehrassahib", "anandsahib", "jaapsahib", "asadivar", "tavprasad"};
    private String[] pathUrls = {
            "https://www.sikhnet.com/gurbani/audio/play/16256/Chaupaee_Sahib.mp3",
            "https://www.sikhnet.com/gurbani/audio/play/7357/Bhai Niranjan Singh - Sukhmani Sahib.mp3",
            "https://www.sikhnet.com/gurbani/audio/play/13429/Giani Thaker Singh - Japji Sahib.mp3",
            "https://www.sikhnet.com/gurbani/audio/play/7220/Bhai Harjinder Singh (Srinagar) - Rehras Sahib.mp3",
            "https://www.sikhnet.com/gurbani/audio/play/11750/Giani Sant Singh Maskeen - Anand Sahib.mp3",
            "https://www.sikhnet.com/gurbani/audio/play/10994/Prof Satnam Singh Sethi - Jaap Sahib_0.mp3",
            "https://www.sikhnet.com/gurbani/audio/play/11939/Bhai Nirmal Singh - Asa Di Vaar.mp3",
            "https://www.sikhnet.com/gurbani/audio/play/38492/swaiyaee.mp3"
    };
    private boolean pause = true;
    private AudioManager mAudioManager;
    private Wait wait = null;
    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(wait != null && wait.isAdded() && wait.isResumed()) {
                wait.dismiss();
                Log.i("Playercheck",""+intent.getExtras().getBoolean("run"));
                if (!serviceStarted)
                    serviceStarted = true;
                if (Mp3PlayerService.oncomplete)
                    serviceStarted = false;
            }
        }
    };
    private Context context;

    public Fragment1() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            display = getActivity().getWindowManager().getDefaultDisplay();
            intent = new Intent(getActivity().getApplicationContext(), Mp3PlayerService.class);
            Log.i("Playercheck", "Intent created");
            context = getActivity().getApplicationContext();
            context.registerReceiver(receiver, new IntentFilter("com.purefaithstudio.gurbani.Mp3Player"));
            mAudioManager = (AudioManager) getActivity().getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
            MainActivity.setTrackerScreenName("path");
            wait = new Wait();
        } catch (Exception e) {
            Log.i("AppNitnem", "cannot create Fragment1");
            e.printStackTrace();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment1, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_one);
        LinearLayoutManager layoutManager = new LinearLayoutManager(rootView.getContext());
        MyArrayAdapter arrayAdapter = new MyArrayAdapter(rootView.getContext(), itemdata);
        try {
            arrayAdapter.setClickListener(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(arrayAdapter);
        } catch (Exception e) {
            Log.i("AppNitnem", "cannot Set Adapter Fragment1");
            e.printStackTrace();
        }

        i = new Intent(rootView.getContext(), Second.class);
        b = new Bundle();
        return rootView;
    }

    @Override
    public void itemClicked(View view, int position) {
        if (view.getId() == R.id.path_play_iconID) {
            if (NetworkConnectionDetector.isConnectingToInternet(getActivity().getApplicationContext())) {
                try {
                    if (!togglePlay) {
                        if (pause || !serviceStarted)
                            play(view, position);
                    } else {
                        if (serviceStarted)
                            stop(view, position);
                    }

                } catch (Exception e) {
                    Log.i("AppNitnem", "cannot play or stop");
                    e.printStackTrace();
                }
            } else
                Toast.makeText(getActivity().getApplicationContext(), "No Internet Connection!!", Toast.LENGTH_SHORT).show();
        } else {
            switch (position) {
                case 0:
                    title = R.string.title_activity_second;
                    largeText = getString(R.string.large_text2);
                    pathText = getString(R.string.chaupai);
                    addToIntent();
                    break;
                case 1:
                    title = R.string.title_activity_third;
                    largeText = getString(R.string.large_text1);
                    pathText = getString(R.string.sukhmani);
                    addToIntent();
                    break;

                case 2:
                    title = R.string.title_activity_fourth;
                    largeText = getString(R.string.large_text3);
                    pathText = getString(R.string.japji_sahib);
                    addToIntent();
                    break;
                case 3:
                    title = R.string.title_activity_fifth;
                    largeText = getString(R.string.large_text4);
                    pathText = getString(R.string.rehras);
                    addToIntent();
                    break;
                case 4:
                    title = R.string.title_activity_sixth;
                    largeText = getString(R.string.large_text5);
                    pathText = getString(R.string.anand_sahib);
                    addToIntent();
                    break;
                case 5:
                    title = R.string.title_activity_seven;
                    largeText = getString(R.string.large_text6);
                    pathText = getString(R.string.jaap_sahib);
                    addToIntent();
                    break;
                case 6:
                    title = R.string.title_activity_eight;
                    largeText = getString(R.string.large_text7);
                    pathText = getString(R.string.asadivar);
                    addToIntent();
                    break;
                case 7:
                    title = R.string.title_activity_nine;
                    largeText = getString(R.string.large_text8);
                    pathText = getString(R.string.tavprasad);
                    addToIntent();
                    break;
            }
        }
    }

    private void addToIntent() {
        try {
            b.putString("key1", largeText);
            b.putString("key2", pathText);
            b.putInt("key3", title);
            i.putExtras(b);
            startActivity(i);
            //pathUrl is not used here
        } catch (Exception e) {
            Log.i("AppNitnem", "Add to intent failed/cannot start Activity Second");
            e.printStackTrace();
        }

    }

    private void stop(View view, int position) {
        togglePlay = false;
        if (Mp3PlayerService.player.isPlaying()) {
            ((ImageView) view).setImageResource(R.drawable.play);
            if (!(currentPosition == position)) {
                Log.i("currentposition","cpos"+currentPosition+"pos:"+position);
                ((ImageView) currentView).setImageResource(R.drawable.play);
                getActivity().getApplicationContext().stopService(intent);
                Log.i("Playercheck", "Service stoped played next");
                serviceStarted = false;
                play(view, position);
            } else {
                Log.i("Playercheck","paused");
                Mp3PlayerService.player.pause();
                pause = true;
                Log.i("Playercheck", "pause called");
            }
        }
    }

    private void play(View view, int position) {
        //start playing now
        togglePlay = true;
        //mp3player=new Mp3PlayerService(position);
        ((ImageView) view).setImageResource(R.drawable.stop_blue);
        if (!(currentPosition == position)) {
            currentPosition = position;
            Log.i("currentposition",""+currentPosition);
            //((ImageView) currentView).setImageResource(R.drawable.play);
            currentView = view;
            Bundle b = new Bundle();
            b.putString("url", pathUrls[position]);
            intent.putExtras(b);
            wait.show(getFragmentManager(), "tag2");
            getActivity().getApplicationContext().startService(intent);
            Log.i("Playercheck", "service started again");
        } else {
            Log.i("currentposition","cpos"+currentPosition+"pos:"+position);
            ((ImageView) currentView).setImageResource(R.drawable.stop_blue);
            Mp3PlayerService.player.start();
            pause = false;
            Log.i("Playercheck", "play again/paused previously");
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (MainActivity.isMyServiceRunning(Mp3PlayerService.class, context))
            getActivity().getApplicationContext().stopService(intent);
        mAudioManager.abandonAudioFocus(this);
        getActivity().getApplicationContext().unregisterReceiver(receiver);
    }

    public Intent getIntent() {
        return intent;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        try {
            if (Mp3PlayerService.player != null) {
                if (focusChange <= 0) {
                    //LOSS -> PAUSE
                    if (Mp3PlayerService.player.isPlaying()) {
                        Mp3PlayerService.player.pause();
                        pause = true;
                        Log.i("Playercheck", "audio focus pause called");
                    }
                } else {
                    //GAIN -> PLAY
                    if (pause && togglePlay) {
                        pause = false;
                        Mp3PlayerService.player.start();
                        Log.i("Playercheck", "audio focus play called");
                    }
                }
            }
        } catch (Exception e) {
            Log.i("AppNitnem", "Error in AudioFocus");
            e.printStackTrace();
        }

        if (Mp3PlayerService.player != null)
            if (focusChange <= 0) {
                //LOSS -> PAUSE
                if (Mp3PlayerService.player.isPlaying()) {
                    Mp3PlayerService.player.pause();
                    pause = true;
                    Log.i("Playercheck", "audio focus pause called");
                }
                // Log.i("Playercheck", "pause called");
            } else {
                //GAIN -> PLAY
                if (pause && togglePlay) {
                    pause = false;
                    Mp3PlayerService.player.start();
                    Log.i("Playercheck", "audio focus play called");
                }
            }
    }

   /* private String getUrl(int position) throws NullPointerException {
        boolean found = false;
        ArrayList<File> files = MainActivity.Files.getFileArrayList();
        String url = "";
        for (File file : files) {
            if (names[position].equals(file.getName())) {
                url = file.getUrl();
                Log.i("Playercheck", "Url founded pos:" + position + "  " + url);
                found = true;
                break;
            }
            Log.i("Playercheck", "Advance for");
        }
        if(!found)
            Log.i("Playercheck", "baani not found url");
        return url;
    }*/
}
