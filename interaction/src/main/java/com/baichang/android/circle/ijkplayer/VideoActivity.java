/*
 * Copyright (C) 2015 Bilibili
 * Copyright (C) 2015 Zhang Rui <bbcallen@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.baichang.android.circle.ijkplayer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TableLayout;
import com.baichang.android.circle.R;
import com.baichang.android.circle.ijkplayer.content.RecentMediaStorage;
import com.baichang.android.circle.ijkplayer.fragments.TracksFragment;
import com.baichang.android.circle.ijkplayer.widget.media.AndroidMediaController;
import com.baichang.android.circle.ijkplayer.widget.media.IjkVideoView;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.misc.ITrackInfo;

public class VideoActivity extends AppCompatActivity implements TracksFragment.ITrackHolder {
  private static final String TAG = "VideoActivity";

  private Uri mVideoUri;

  private IjkVideoView mVideoView;
  private TableLayout mHudView;

  private boolean mBackPressed;

  public static Intent newIntent(Context context, String videoPath) {
    Intent intent = new Intent(context, VideoActivity.class);
    intent.putExtra("videoPath", videoPath);
    return intent;
  }

  public static void intentTo(Context context, String videoPath) {
    context.startActivity(newIntent(context, videoPath));
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_player);

    // handle arguments
    String videoPath = getIntent().getStringExtra("videoPath");


    if (!TextUtils.isEmpty(videoPath)) {
      new RecentMediaStorage(this).saveUrlAsync(videoPath);
    }

    // init UI
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    ActionBar actionBar = getSupportActionBar();
    AndroidMediaController mediaController = new AndroidMediaController(this, false);
    mediaController.setSupportActionBar(actionBar);

    mHudView = (TableLayout) findViewById(R.id.hud_view);
    // init player
    IjkMediaPlayer.loadLibrariesOnce(null);
    IjkMediaPlayer.native_profileBegin("libijkplayer.so");

    mVideoView = (IjkVideoView) findViewById(R.id.video_view);
    mVideoView.setMediaController(mediaController);
    mVideoView.setHudView(mHudView);
    // prefer mVideoPath
    if (videoPath != null) {
      mVideoView.setVideoPath(videoPath);
    } else if (mVideoUri != null) {
      mVideoView.setVideoURI(mVideoUri);
    } else {
      Log.e(TAG, "Null Data Source\n");
      finish();
      return;
    }
    mVideoView.start();
  }

  @Override public void onBackPressed() {
    mBackPressed = true;

    super.onBackPressed();
  }

  @Override protected void onStop() {
    super.onStop();

    if (mBackPressed || !mVideoView.isBackgroundPlayEnabled()) {
      mVideoView.stopPlayback();
      mVideoView.release(true);
      mVideoView.stopBackgroundPlay();
    } else {
      mVideoView.enterBackground();
    }
    IjkMediaPlayer.native_profileEnd();
  }

  @Override public ITrackInfo[] getTrackInfo() {
    if (mVideoView == null) return null;

    return mVideoView.getTrackInfo();
  }

  @Override public void selectTrack(int stream) {
    mVideoView.selectTrack(stream);
  }

  @Override public void deselectTrack(int stream) {
    mVideoView.deselectTrack(stream);
  }

  @Override public int getSelectedTrack(int trackType) {
    if (mVideoView == null) return -1;

    return mVideoView.getSelectedTrack(trackType);
  }
}
