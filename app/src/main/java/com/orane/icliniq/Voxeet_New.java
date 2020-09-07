package com.orane.icliniq;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


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
import com.voxeet.sdk.services.conference.information.ConferenceInformation;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Voxeet_New extends AppCompatActivity {

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
    @NonNull
    @Bind(R.id.conference_name)
    EditText conference_name;
    @Bind(R.id.user_name)
    EditText user_name;
    @Bind(R.id.participants)
    EditText participants;

    String room_name, cons_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voxeet_new);

        ButterKnife.bind(this);

        VoxeetSDK.initialize("OXEyM3VpZDZjYjVwdg==", "cGY2ZXM2M2ExaGZ1a3JkaWhzOGJzYmVybQ==");

        try {
            Intent intent = getIntent();
            cons_name = intent.getStringExtra("cons_user_name");
            room_name = intent.getStringExtra("conf_name");

            System.out.println("room_name------------->" + room_name);
            System.out.println("cons_name------------->" + cons_name);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*room_name = "5da8875242a956e2f1e0e31f8936187786a8571117afb8ff566e511098b62a5a";
        cons_name = "Mohan";

        System.out.println("room_name--------" + room_name);
        System.out.println("cons_name--------" + cons_name);
*/

        //adding the user_name, login and logout views related to the open/close and conference flow
        add(views, R.id.login);
        add(views, R.id.logout);

        add(buttonsNotLoggedIn, R.id.login);
        add(buttonsNotLoggedIn, R.id.user_name);

        add(buttonsInConference, R.id.logout);

        add(buttonsNotInConference, R.id.logout);

        add(views, R.id.join);

        add(buttonsNotInConference, R.id.join);

        add(views, R.id.leave);

        add(buttonsInConference, R.id.leave);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateViews();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA}, 0x20);
        }

        VoxeetSDK.instance().register(this);
    }

    private Voxeet_New add(List<View> list, int id) {
        list.add(findViewById(id));
        return this;
    }

    private void updateViews() {
        setEnabled(views, false);

        //if the user is not connected, we will only enabled the not logged
        if (!VoxeetSDK.session().isSocketOpen()) {
            setEnabled(buttonsNotLoggedIn, true);
            return;
        }

        ConferenceInformation current = VoxeetSDK.conference().getCurrentConference();
        //we can now add the logic to manage our basic state
        if (null != current && VoxeetSDK.conference().isLive()) {
            setEnabled(buttonsInConference, true);
        } else {
            setEnabled(buttonsNotInConference, true);
        }
    }

    private ErrorPromise error() {
        return error -> {
            Toast.makeText(Voxeet_New.this, "ERROR...", Toast.LENGTH_SHORT).show();
            error.printStackTrace();
            updateViews();
        };
    }

    private void setEnabled(@NonNull List<View> views, boolean enabled) {
        for (View view : views) view.setEnabled(enabled);
    }

    @OnClick(R.id.login)
    public void onLogin() {

        VoxeetSDK.session().open(new ParticipantInfo(cons_name, "", ""))
                .then((result, solver) -> {
                    Toast.makeText(Voxeet_New.this, "started...", Toast.LENGTH_SHORT).show();
                    updateViews();
                })
                .error(error());
    }

    @OnClick(R.id.logout)
    public void onLogout() {
        VoxeetSDK.session().close()
                .then((result, solver) -> {
                    Toast.makeText(Voxeet_New.this, "logout done", Toast.LENGTH_SHORT).show();
                    updateViews();
                }).error(error());
    }

    @OnClick(R.id.join)
    public void onJoin() {


        VoxeetSDK.conference().create(room_name)
                .then((PromiseExec<CreateConferenceResult, Conference>) (result, solver) ->
                        solver.resolve(VoxeetSDK.conference().join(result.conferenceId)))
                .then((result, solver) -> {
                    Toast.makeText(Voxeet_New.this, "started...", Toast.LENGTH_SHORT).show();
                    updateViews();
                })
                .error(error());

  /*      VoxeetSDK.conference().create(conference_name.getText().toString())
                .then((ThenPromise<CreateConferenceResult, Conference>) res -> VoxeetSDK.conference().join(res.conferenceId))
                .then(conference -> {
                    Toast.makeText(MainActivity.this, "started...", Toast.LENGTH_SHORT).show();
                    updateViews();
                })
                .error(error());*/
    }

    @OnClick(R.id.leave)
    public void onLeave() {
        VoxeetSDK.conference().leave()
                .then((result, solver) -> updateViews()).error(error());
    }


    @OnClick(R.id.startVideo)
    public void onStartVideo() {
        VoxeetSDK.conference().startVideo()
                .then((result, solver) -> updateViews())
                .error(error());


    }

    @OnClick(R.id.stopVideo)
    public void onStopVideo() {
        VoxeetSDK.conference().stopVideo()
                .then((result, solver) -> updateViews())
                .error(error());
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
        for (Participant user : VoxeetSDK.conference().getParticipants()) {
            boolean isLocal = user.getId().equals(VoxeetSDK.session().getParticipantId());
            MediaStream stream = user.streamsHandler().getFirst(MediaStreamType.Camera);

            VideoView video = isLocal ? this.video : this.videoOther;

            if (null != stream && !stream.videoTracks().isEmpty()) {
                video.setVisibility(View.VISIBLE);
               // video.attach(user.getId(), stream);
            }
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
}
