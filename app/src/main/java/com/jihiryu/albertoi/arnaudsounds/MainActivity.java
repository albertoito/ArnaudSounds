package com.jihiryu.albertoi.arnaudsounds;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    static int MIN_VOL = 8;
    MediaPlayer mMediaPlayer = null;
    AudioManager mAudioManager = null;
    boolean snackVolumeActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, getString(R.string.sb_entre_lineas), Snackbar.LENGTH_LONG)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                playByRes(R.raw.por_favor);
                            }
                        }).show();
            }
        });

        setButtons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        releasePlayer();

        int id = item.getItemId();
        switch (id) {
            case R.id.action_share:
                shareApp();
                return true;
            case R.id.action_arnaud:
                showContactDialog();
                return true;
            case R.id.action_about:
                showAboutDialog();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMediaPlayer != null && mMediaPlayer.isPlaying())
            mMediaPlayer.stop();
    }

    private void setButtons() {
        setButton(R.id.btnNo);
        setButton(R.id.btnNooo);
        setButton(R.id.btnPorfavor);
        setButton(R.id.btnMonos);
        setButton(R.id.btnHacerMierda);
        setButton(R.id.btnKamaeMierda);
        setButton(R.id.btnCuandoKarate);
        setButton(R.id.btnNoEsKarate);
        setButton(R.id.btnNoEsBaile);
        setButton(R.id.btnArnaud);
    }

    private void setButton(int btnId) {
        AppCompatButton btn = (AppCompatButton) findViewById(btnId);
        if (btn != null)
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (volumeIsLow())
                        increaseVolume(view);
                    play(view);
                }
            });
    }

    private void play(View view) {
        int viewId = view.getId();
        playByRes(getRawId(viewId));
    }

    private void playByRes(int rawId) {
        releasePlayer();
        mMediaPlayer = MediaPlayer.create(this, rawId);
        mMediaPlayer.setVolume(1, 1);
        mMediaPlayer.start();
    }

    private int getRawId(int viewId) {
        switch (viewId) {
            case R.id.btnNo:
                return R.raw.no;
            case R.id.btnNooo:
                return R.raw.nooo;
            case R.id.btnPorfavor:
                return R.raw.por_favor;
            case R.id.btnMonos:
                return R.raw.monos;
            case R.id.btnHacerMierda:
                return R.raw.hacer_mierda;
            case R.id.btnKamaeMierda:
                return R.raw.kamae_mierda;
            case R.id.btnCuandoKarate:
                return R.raw.cuando_karate;
            case R.id.btnNoEsKarate:
                return R.raw.no_es_karate;
            case R.id.btnNoEsBaile:
                return R.raw.no_es_baile;
            case R.id.btnArnaud:
                return R.raw.arnaud;
            case R.id.action_arnaud:
                return R.raw.arnaud_pronuntiation;
        }

        return R.raw.por_favor;
    }

    private void releasePlayer() {
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying())
                mMediaPlayer.stop();

            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private boolean volumeIsLow() {
        return mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) < MIN_VOL;
    }

    private void increaseVolume(View view) {
        if (!snackVolumeActive) {
            snackVolumeActive = true;
            Snackbar.make(view, getString(R.string.sb_vol_mono), Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackVolumeActive = false;
                            playByRes(R.raw.por_favor);
                        }
                    }).show();
        }
    }

    private void shareApp() {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.url_share_app));
            startActivity(Intent.createChooser(intent, getString(R.string.share_title)));
        } catch (Exception e) {

        }
    }

    private void openUrl(int urlId) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(urlId)));
        startActivity(intent);
    }

    private void showAboutDialog() {
        AlertDialog about = new AlertDialog.Builder(MainActivity.this).setTitle(R.string.action_about).create();
        about.setMessage(getString(R.string.version));
        about.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        about.show();
    }

    private void showContactDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Seleccione el medio").setItems(R.array.contactos, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                openUrlFromDialog(which);
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void openUrlFromDialog(int index) {
        switch (index) {
            case 0:
                openUrl(R.string.url_koi);
                break;
            case 1:
                openUrl(R.string.url_facebook);
                break;
            case 2:
                openUrl(R.string.url_linkedin);
                break;
            case 3:
                openUrl(R.string.url_wordpress);
                break;
        }
    }
}