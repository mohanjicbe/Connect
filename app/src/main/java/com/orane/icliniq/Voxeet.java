package com.orane.icliniq;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.orane.icliniq.Model.BaseActivity;
import com.orane.icliniq.Model.Model;
import com.voxeet.VoxeetSDK;
import com.voxeet.android.media.MediaStream;
import com.voxeet.android.media.MediaStreamType;
import com.voxeet.promise.solve.ErrorPromise;
import com.voxeet.promise.solve.PromiseExec;
import com.voxeet.sdk.events.v2.ParticipantAddedEvent;
import com.voxeet.sdk.events.v2.ParticipantUpdatedEvent;
import com.voxeet.sdk.events.v2.StreamAddedEvent;
import com.voxeet.sdk.events.v2.StreamRemovedEvent;
import com.voxeet.sdk.events.v2.StreamUpdatedEvent;
import com.voxeet.sdk.json.ParticipantInfo;
import com.voxeet.sdk.models.Conference;
import com.voxeet.sdk.models.Participant;
import com.voxeet.sdk.models.v1.CreateConferenceResult;
import com.voxeet.sdk.views.VideoView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class Voxeet extends BaseActivity {
    @NonNull
    protected List<View> views = new ArrayList<>();
    @NonNull
    protected List<View> buttonsNotLoggedIn = new ArrayList<>();
    @NonNull
    protected List<View> buttonsInConference = new ArrayList<>();
    @NonNull
    protected List<View> buttonsNotInConference = new ArrayList<>();
    @Bind(R.id.video)
    protected VideoView video;
    @Bind(R.id.videoOther)
    protected VideoView videoOther;
    int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    MediaStream stream_array[] = new MediaStream[10];
    String video_array[] = new String[10];
    String caller_name[] = new String[10];


    LinearLayout users_layout;
    @NonNull
    @Bind(R.id.conference_name)
    EditText conference_name;
    @Bind(R.id.user_name)
    EditText user_name;
    @Bind(R.id.participants)
    EditText participants;

    String cons_user_name, conf_name, str_response;
    Button img_video_start, img_video_stop, startVideo, stopVideo;
    ImageView img_switch_cam, img_mic_on, img_mic_off, img_video_on, img_video_off;
    ToggleButton togg_cam;
    LinearLayout video_call_placeholder,bottom_navi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voxeet_test);

        ButterKnife.bind(this);

        //VoxeetSDK.initialize("OXEyM3VpZDZjYjVwdg==", "cGY2ZXM2M2ExaGZ1a3JkaWhzOGJzYmVybQ==");
        VoxeetSDK.initialize(Model.voxeet_cons_key, Model.voxeet_cons_alias_name);

        try {
            Intent intent = getIntent();
            cons_user_name = intent.getStringExtra("cons_user_name");
            conf_name = intent.getStringExtra("conf_name");

            System.out.println("cons_user_name------------->" + cons_user_name);
            System.out.println("conf_name------------->" + conf_name);
        } catch (Exception e) {
            e.printStackTrace();
        }


/*
        cons_user_name = "Mohan_app";
        conf_name = "icliniq";
*/



        bottom_navi = findViewById(R.id.bottom_navi);
        img_video_start = findViewById(R.id.img_video_start);
        img_video_stop = findViewById(R.id.img_video_stop);
        img_video_off = findViewById(R.id.img_video_off);
        img_video_on = findViewById(R.id.img_video_on);
        img_switch_cam = findViewById(R.id.img_switch_cam);
        togg_cam = findViewById(R.id.togg_cam);
        video_call_placeholder = findViewById(R.id.video_call_placeholder);
        img_mic_on = findViewById(R.id.img_mic_on);
        img_mic_off = findViewById(R.id.img_mic_off);
        users_layout = findViewById(R.id.users_layout);

        togg_cam.setText(null);
        togg_cam.setTextOn(null);
        togg_cam.setTextOff(null);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
            }
        }
       /* if (ContextCompat.checkSelfPermission(this, Manifest.permission.MODIFY_AUDIO_SETTINGS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.MODIFY_AUDIO_SETTINGS)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.MODIFY_AUDIO_SETTINGS}, MY_PERMISSIONS_REQUEST_CAMERA);
            }

        }*/

        //--------- INitializa --------------------
        VoxeetSDK.session().open(new ParticipantInfo(cons_user_name, "", ""))
                .then((result, solver) -> {
                    Toast.makeText(Voxeet.this, "started...", Toast.LENGTH_SHORT).show();
                    System.out.println("Started....................");
                    //updateViews();

                    bottom_navi.setVisibility(View.VISIBLE);


                    VoxeetSDK.conference().create(conf_name)
                            .then((PromiseExec<CreateConferenceResult, Conference>) (result2, solver2) ->
                                    solver.resolve(VoxeetSDK.conference().join(result2.conferenceId)))
                            .then((result2, solver2) -> {

                                VoxeetSDK.conference().startVideo();

                                // simulcast(@NonNull List< ParticipantQuality > requested)
                                // VoxeetSDK.conference().simulcast(participants HD);

                                VoxeetSDK.audio().setSpeakerMode(true);

/*                                VoxeetSDK.conference().startVideo()
                                        .then((result3, solver3) -> {
                                            VoxeetSDK.conference().startVideo();
                                            System.out.println("Video Started....................");
                                            VoxeetSDK.audio().setSpeakerMode(true);
                                        })
                                        .error(error());*/

                            })
                            .error(error());
                })
                .error(error());
        //--------- INitializa --------------------

        img_video_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                VoxeetSDK.conference().startVideo()
                        .then((result, solver) -> {
                        })
                        .error(error());

/*                img_video_off.setVisibility(View.VISIBLE);
                img_video_on.setVisibility(View.GONE);*/

                img_video_start.setVisibility(View.GONE);
                img_video_stop.setVisibility(View.VISIBLE);
                img_video_off.setVisibility(View.VISIBLE);

                Toast.makeText(Voxeet.this, "You have joined with consultation..", Toast.LENGTH_SHORT).show();

            }
        });

        img_video_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ask_end();
            }
        });

        img_video_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VoxeetSDK.conference().stopVideo()
                        .then((result, solver) -> {
                            img_video_on.setVisibility(View.VISIBLE);
                            img_video_off.setVisibility(View.GONE);

                            // video.setVisibility(View.GONE);

                            //Toast.makeText(Voxeet.this, "Consultation ended...", Toast.LENGTH_SHORT).show();

                        })
                        .error(error());
            }
        });

        img_video_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VoxeetSDK.conference().startVideo()
                        .then((result, solver) -> {
                            img_video_on.setVisibility(View.GONE);
                            img_video_off.setVisibility(View.VISIBLE);

                            // video.setVisibility(View.VISIBLE);

                            //Toast.makeText(Voxeet.this, "Consultation ended...", Toast.LENGTH_SHORT).show();
                        })
                        .error(error());
            }
        });

        img_mic_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_mic_on.setVisibility(View.VISIBLE);
                img_mic_off.setVisibility(View.GONE);

                VoxeetSDK.conference().mute(false);


                Toast.makeText(Voxeet.this, "Volume has been UnMute...", Toast.LENGTH_SHORT).show();
            }
        });


        img_mic_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_mic_on.setVisibility(View.GONE);
                img_mic_off.setVisibility(View.VISIBLE);

                VoxeetSDK.conference().mute(true);
                VoxeetSDK.audio().setSpeakerMode(true);

                Toast.makeText(Voxeet.this, "Volume has been Mute...", Toast.LENGTH_SHORT).show();
            }
        });


        img_switch_cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                VoxeetSDK.mediaDevice().switchCamera();

                System.out.println("Switch Camera..........");
            }
        });

        togg_cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean on = ((ToggleButton) view).isChecked();

                if (on) {
                    VoxeetSDK.conference().stopVideo()
                            .then((result, solver) -> {
                                img_video_on.setVisibility(View.VISIBLE);
                                img_video_off.setVisibility(View.GONE);

                                video.setVisibility(View.GONE);

                                //Toast.makeText(Voxeet.this, "Consultation ended...", Toast.LENGTH_SHORT).show();

                            })
                            .error(error());
                } else {
                    VoxeetSDK.conference().startVideo()
                            .then((result, solver) -> {
                                img_video_on.setVisibility(View.GONE);
                                img_video_off.setVisibility(View.VISIBLE);

                                // video.setVisibility(View.VISIBLE);

                                //Toast.makeText(Voxeet.this, "Consultation ended...", Toast.LENGTH_SHORT).show();
                            })
                            .error(error());
                }
                System.out.println("Switch Camera..........");
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA}, 0x20);
        }

        VoxeetSDK.instance().register(this);
    }

    private Voxeet add(List<View> list, int id) {
        list.add(findViewById(id));
        return this;
    }

    private ErrorPromise error() {
        try {
            return error -> {
                Toast.makeText(Voxeet.this, "ERROR...", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
                //updateViews();
            };
        }
        catch (Exception e){
            Toast.makeText(Voxeet.this, "video Error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(StreamAddedEvent event) {
        updateStreams();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(StreamUpdatedEvent event) {
        updateStreams();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(StreamRemovedEvent event) {
        updateStreams();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ParticipantAddedEvent event) {
        updateUsers();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ParticipantUpdatedEvent event) {
        updateUsers();
    }




    private void updateStreams() {

        users_layout.removeAllViews();

        Integer i = 1;

        for (Participant user : VoxeetSDK.conference().getParticipants()) {

            System.out.println("Participants----" + VoxeetSDK.session().getParticipantId());

            //VoxeetSDK.conference().simulcast()

            System.out.println("user_id----" + user.getId());
            System.out.println("user_info----" + user.getInfo());

            boolean isLocal = user.getId().equals(VoxeetSDK.session().getParticipantId());
            MediaStream stream = user.streamsHandler().getFirst(MediaStreamType.Camera);

            //VoxeetSDK.conference().simulcast(HD);
            stream_array[i] = stream;
            video_array[i] = "" + user.getId();

            VideoView video = isLocal ? this.video : this.videoOther;
            //VideoView video = isLocal ? this.videoOther : this.video;

            if (null != stream && !stream.videoTracks().isEmpty()) {
                videoOther.setVisibility(View.VISIBLE);
                videoOther.attach(user.getId(), stream);
                System.out.println("Stream----" + stream);

                //---------------------------------------
                LayoutInflater layoutInflater2 = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater2.inflate(R.layout.videoview, null);

                VideoView video_view = addView.findViewById(R.id.video);
                TextView user_name = addView.findViewById(R.id.user_name);
                TextView tv_user_id = addView.findViewById(R.id.user_id);

                //user_name.setText("" + i);
                tv_user_id.setText("" + i);

                video_view.attach(user.getId(), stream);

                //-------- Name Update --------------------------------------
                List<Participant> participants = VoxeetSDK.conference().getParticipants();
                String part_name = user.getInfo().getName();
                user_name.setText(part_name);
                //-------- Name Update --------------------------------------



                video_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View parent = (View) v.getParent();
                        TextView stream_text = parent.findViewById(R.id.user_id);
                        String stream_text_val = stream_text.getText().toString();
                        Integer stream_int = Integer.parseInt(stream_text_val);

                        MediaStream mstr_value = stream_array[stream_int];
                        String varray = video_array[stream_int];

                        System.out.println("mstr_value------" + mstr_value);
                        System.out.println("varray------" + varray);

                        videoOther.attach(varray, mstr_value);
                    }
                });

                users_layout.addView(addView);
                //------------------------------------
            }
            i++;
        }
    }


    public void updateUsers() {
        List<Participant> participants = VoxeetSDK.conference().getParticipants();
        List<String> names = new ArrayList<>();

        for (Participant participant : participants) {
            names.add(participant.getInfo().getName());
        }

        this.participants.setText(TextUtils.join(",", names));
    }

    public void ask_end() {

        final Context context = this;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder
                .setMessage("Do you want to end this consultation.?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        VoxeetSDK.conference().stopVideo()
                                .then((result, solver) -> {

                                    VoxeetSDK.conference().leave()
                                            .then((result2, solver2) -> {

                                                VoxeetSDK.session().close()
                                                        .then((result22, solver22) -> {
                                                            //Toast.makeText(Voxeet.this, "Consultation ended", Toast.LENGTH_SHORT).show();
                                                            finish();
                                                        }).error(error());

                                            }).error(error());
                                }).error(error());
                    }
                })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}