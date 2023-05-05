package `in`.fiberstory.tfsplaytv.activities

import  `in`.fiberstory.tfsplaytv.R
import `in`.fiberstory.tfsplaytv.utility.AddWatchHistoryService
import `in`.fiberstory.tfsplaytv.utility.AudioFocusUtils
import `in`.fiberstory.tfsplaytv.utility.DemoUtil
import `in`.fiberstory.tfsplaytv.utility.Networkservice
import android.annotation.SuppressLint
import android.content.*
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.Display
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.drm.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.*
import com.google.android.exoplayer2.util.EventLogger
import com.google.android.exoplayer2.util.Log
import com.google.android.exoplayer2.util.Util
import java.util.*

//
class ExoplayerActivity : FragmentActivity() {
    protected var playerView: PlayerView? = null
    protected var player: SimpleExoPlayer? = null
    private var timer: Timer? = null

    //private Toast toast;
    var count = 1
    var isOn = true
    var playing_stage = 1
    var internet_lost_count = 0
    var txt_connection: TextView? = null
    var durationSet = false
    var realDurationMillis: Long = 0
   // var movie: MovieDetailModel? = null

    var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == BROADCAST) {
                if (intent.getStringExtra("online_status") == "true") {
                    isOn = true
                    if (playing_stage == 1) {
                        internet_lost_count = 0
                        if (player != null) {
                            run {
                                /*if(ib_pause.getVisibility()==View.VISIBLE){
                            player.play();
                            }else if(ib_play.getVisibility()==View.VISIBLE) {
                                player.pause();
                            }*/
                                // if(count!=0) {
                                //player.play();
                                resumeTrack()
                            }
                            Log.e("resuming", "playback")
                            if (count == 0) {
                                /*if (toast != null) {
                                    toast.cancel();
                                }
                                toast = Toast.makeText(ExoplayerActivity.this, "Connection restored", Toast.LENGTH_LONG);
                                toast.show();*/
                                //if(!player.isPlaying()) {
                                txt_connection!!.text = "Connection restored"
                                txt_connection!!.visibility = View.VISIBLE
                                //}
                                count++
                            } else {
                                txt_connection!!.visibility = View.GONE
                            }
                        }
                    } else {
                        if (player != null) {
                            player!!.pause()
                        }
                    }
                } else {
                    isOn = false
                    if (playing_stage == 1) {
                        //if (durationSet) {
                        if (player != null) {
                            Log.e("buffered", "" + player!!.bufferedPosition)
                            Log.e("played", "" + player!!.currentPosition)
                            Log.e("duration", "" + player!!.duration)
                            if (player!!.duration == player!!.bufferedPosition) {
                                Log.e("all", "buffered")
                                resumeTrack()
                                txt_connection!!.text = "Waiting for internet connection"
                                txt_connection!!.visibility = View.GONE
                                //count = 0;
                                // }else if (Math.subtractExact(player.getBufferedPosition(), player.getCurrentPosition()) > 3000) {
                            } else if (player!!.bufferedPosition - player!!.currentPosition > 3000) {
                                resumeTrack()
                                //player.play();
                                /*if (toast != null) {
                                    toast.cancel();
                                }*/txt_connection!!.text = "Waiting for internet connection"
                                txt_connection!!.visibility = View.GONE
                            } else {
                                player!!.pause()
                                /*if (toast != null) {
                                    toast.cancel();
                                }
                                toast = Toast.makeText(ExoplayerActivity.this, "Waiting for internet connection", Toast.LENGTH_LONG);
                                toast.show();*/internet_lost_count++
                                if (internet_lost_count == 3) {
                                    txt_connection!!.text = "Waiting for internet connection"
                                    txt_connection!!.visibility = View.VISIBLE
                                    count = 0
                                }
                            }
                        }
                        // }
                    } else {
                        if (player != null) {
                            player!!.pause()
                        }
                    }
                }
            }
        }
    }

    //    private List<MediaItem> mediaItems;
    private var startWindow = 0

    private var startAutoPlay = true
    private var startPosition: Long = 0
    var url: String? = null

    //    private DataSource.Factory mediaDataSourceFactory;
    var mediaItem: MediaItem? = null
    var mediaSource: MediaSource? = null
    var progress_circular: ProgressBar? = null
    private var mainHandler: Handler? = null
    private val eventLogger: EventLogger? = null
    var PLAYBACK_TYPE = 0 //  1 -> trailer, 2-> Non Drm content 3-> DRM content 4-> Live

    var RIntent: Intent? = null
    var isFestivalMovie = false
    private var timeInterval = 30
    var ib_play: ImageButton? = null
    var ib_pause: ImageButton? = null
    var exo_ffwd: ImageButton? = null
    var exo_rew: ImageButton? = null
    var exo_next: ImageButton? = null
    var exo_prev: ImageButton? = null
    var playurl: String? = null
    var drmLicenseUrl: String? = null
    var token_key: String? = null
    var token_value: String? = null
    var drmSchemeExtra: String? = null
    var haveStartPosition = false
    private var contentId = 0
    private var isBackPressed = false
    var sharedPreferences: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null
    val BROADCAST = "checkinternet"
    var intentFilter: IntentFilter? = null
    var serviceIntent: Intent? = null

//    int WatchDuration;


    //    int WatchDuration;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        window.addFlags(Display.FLAG_SUPPORTS_PROTECTED_BUFFERS)
        /* CheckNetwork network = new CheckNetwork(getApplicationContext());
        network.registerNetworkCallback();
*/playing_stage = 0
        intentFilter = IntentFilter()
        intentFilter!!.addAction(BROADCAST)
        LocalBroadcastManager.getInstance(this@ExoplayerActivity).registerReceiver(
            broadcastReceiver,
            intentFilter!!
        )
        Log.e("start", "service")
        serviceIntent = Intent(this, Networkservice::class.java)

        /*if (Networkservice.isOnline(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), "true", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(getApplicationContext(), "false", Toast.LENGTH_SHORT).show();
*/setContentView( `in`.fiberstory.tfsplaytv.R.layout.activity_exoplayer)
        RIntent = intent
        mainHandler = Handler()
        txt_connection = findViewById<View>(R.id.txt_connection) as TextView

//        playurl = "https://s3-eu-west-1.amazonaws.com/conaxconnect-public/content/ssp/tfs5mlsh/dash/stream.mpd";
//        drmLicenseUrl = "https://tfs5mlsh.anycast.nagra.com/TFS5MLSH/wvls/contentlicenseservice/v1/licenses";
//        token_key = "nv-authorizations";
//        token_value = "eyJraWQiOiIxMDgzMjAiLCJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ2ZXIiOiIxLjAiLCJ0eXAiOiJDb250ZW50QXV0aFoiLCJleHAiOjE2MDgwMzcyMDAsImNvbnRlbnRSaWdodHMiOlt7ImR1cmF0aW9uIjo3MjAwLCJjb250ZW50SWQiOiJWT0QtQ09OVEVOVElEMSIsInN0YXJ0IjoiMjAxOS0xMS0xM1QxMjowMDowMFoiLCJzdG9yYWJsZSI6ZmFsc2UsImVuZCI6IjIwMjAtMTItMzFUMTI6MDA6MDBaIiwiZGVmYXVsdFVzYWdlUnVsZXMiOnsibWluTGV2ZWwiOjAsIndhdGVybWFya2luZ0VuYWJsZWQiOmZhbHNlLCJpbWFnZUNvbnN0cmFpbnQiOmZhbHNlLCJoZGNwVHlwZSI6IlRZUEVfMCIsInVuY29tcHJlc3NlZERpZ2l0YWxDYXBwaW5nUmVzb2x1dGlvbiI6Ik5PX1JFU1RSSUNUSU9OUyIsInVucHJvdGVjdGVkQW5hbG9nT3V0cHV0Ijp0cnVlLCJhbmFsb2dDYXBwaW5nUmVzb2x1dGlvbiI6Ik5PX1JFU1RSSUNUSU9OUyIsImhkY3AiOmZhbHNlLCJkZXZpY2VDYXBwaW5nUmVzb2x1dGlvbiI6Ik5PX1JFU1RSSUNUSU9OUyIsImRpZ2l0YWxPbmx5IjpmYWxzZSwidW5wcm90ZWN0ZWREaWdpdGFsT3V0cHV0Ijp0cnVlfX1dfQ.rhUpMuiG5Wd-0Xr2y1Yy2R5bnO4X0dByqyIZ3jljSNE";
//        drmSchemeExtra = "widevine";

//        token_key = "nv-authorizations";
//        drmSchemeExtra = "widevine";
//        playurl =  "https://plexigo-nagra-test.s3-eu-west-1.amazonaws.com/WET6/Trl_Ishq_Malayalam.mpd";
//        drmLicenseUrl = "https://ufo2b7je.anycast.nagra.com/UFO2B7JE/wvls/contentlicenseservice/v1/licenses";
//        token_value = "eyJraWQiOiI4MTUxOTgiLCJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ2ZXIiOiIxLjAiLCJ0eXAiOiJDb250ZW50QXV0aFoiLCJjb250ZW50UmlnaHRzIjpbeyJjb250ZW50SWQiOiJXRVQ2IiwidXNhZ2VSdWxlc1Byb2ZpbGVJZCI6IlRlc3QifV19.uYdSUeS-DiWDMS-RgSUZrVDLmZPbIiUhr2K8_00zCSk";
//        PLAYBACK_TYPE = 3;
        sharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(this)
        playerView = findViewById(R.id.player_view)
        playerView!!.requestFocus()
        //        mediaDataSourceFactory = buildHttpDataSourceFactory(true);
        if (intent.hasExtra("licenceUrl") && intent.getStringExtra("licenceUrl") != null) {
            // DRM content
            PLAYBACK_TYPE = 3
            token_key = "nv-authorizations"
            drmSchemeExtra = "widevine"
            url = intent.getStringExtra("DashUrl")
            drmLicenseUrl = intent.getStringExtra("licenceUrl")
            token_value = intent.getStringExtra("token")
        } else if (intent.hasExtra("movieUrl") && intent.getStringExtra("movieUrl") != null) {
            // non-DRM movie content
            url = intent.getStringExtra("movieUrl")
            PLAYBACK_TYPE = 2
        } else if (intent.hasExtra("LiveEventUrl") && intent.getStringExtra("LiveEventUrl") != null) {
            //live events
            url = intent.getStringExtra("LiveEventUrl")
            isFestivalMovie = intent.getBooleanExtra("isFestivalMovie", false)
            if (isFestivalMovie) {
                Log.e("is", "festival")
            } else {
                Log.e("not a", "festival")
            }
            timeInterval = intent.getIntExtra("timeInterval", 30)
            //           contentId = getIntent().getIntExtra("contentId", 0);
            PLAYBACK_TYPE = 4
            //            Moviename = getIntent().getStringExtra("EventName");
//            Story = getIntent().getStringExtra("story");
//            movie_title.setText(Moviename);
//            storyline.setText(Story);
//            isLiveContent = true;
        } else { //trailer
            url = intent.getStringExtra("TrailerUrl")
            PLAYBACK_TYPE = 1
            //            url ="https://plexigoqa.s3.ap-south-1.amazonaws.com/Content/Movies/MV_Ishq_Malayalam_16083/Trailer/Trl_Ishq_Malayalam.m3u8";
//            Moviename = getIntent().getStringExtra("movieName");
//            Story = getIntent().getStringExtra("story");
//            movie_title.setText(Moviename);
//            storyline.setText(Story);
        }
        if (intent.hasExtra("WatchDuration") && intent.getIntExtra("WatchDuration", 0) != 0) {
            val seconds = intent.getIntExtra("WatchDuration", 0)
            if (seconds != 0) {
                startPosition = (seconds * 1000).toLong()
                //                Toast.makeText(this, ""+startPosition, Toast.LENGTH_SHORT).show();
                haveStartPosition = true
            }
        }
        if (intent.hasExtra("contentId")) {
            contentId = intent.getIntExtra("contentId", 0)
        }
        playerView = findViewById(R.id.player_view)
        progress_circular = findViewById(R.id.progress_circular)
        ib_play = playerView!!.findViewById<View>(com.google.android.exoplayer2.R.id.exo_play) as ImageButton
        ib_pause = playerView!!.findViewById<View>(com.google.android.exoplayer2.R.id.exo_pause) as ImageButton
        exo_ffwd = playerView!!.findViewById<View>(com.google.android.exoplayer2.R.id.exo_ffwd) as ImageButton
        exo_rew = playerView!!.findViewById<View>(com.google.android.exoplayer2.R.id.exo_rew) as ImageButton
        /*exo_next = (ImageButton)playerView.findViewById(R.id.exo_next);
        exo_prev = (ImageButton)playerView.findViewById(R.id.exo_prev);
        exo_next.setVisibility(View.GONE);
        exo_prev.setVisibility(View.GONE);*/
    }

    override fun onBackPressed() {
        if (PLAYBACK_TYPE == 2 || PLAYBACK_TYPE == 3 || PLAYBACK_TYPE == 4) {
            isBackPressed = true
            startPosition = Math.max(0, player!!.contentPosition)
            var seconds = (startPosition / 1000).toInt()
            if (seconds == (player!!.duration / 1000).toInt()) {
                seconds = 0
            }
            RIntent!!.putExtra("playback", seconds)
            setResult(RESULT_OK, RIntent)
        }
        super.onBackPressed()
    }

    override fun onStart() {
        super.onStart()
        Log.e("on", "start")
        /*IntentFilter filter = new IntentFilter(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        registerReceiver(broadcastReceiver, filter);*/
    }

    override fun onRestart() {
        super.onRestart()
        Log.e("on", "restart")
    }

    fun buildHttpDataSourceFactory(bandwidthMeter: DefaultBandwidthMeter?): HttpDataSource.Factory? {
        return DefaultHttpDataSourceFactory("Android", bandwidthMeter)
    }

    override fun onResume() {
        super.onResume()
        AudioFocusUtils.getFocus(this)
        playing_stage = 1
        if (PLAYBACK_TYPE == 2 || PLAYBACK_TYPE == 3 || PLAYBACK_TYPE == 4) {
           // getPlaybackLastDuration()
               initializePlayer()
        } else {
            //if (Util.SDK_INT > 23) {
            initializePlayer()
            if (playerView != null) {
                playerView!!.onResume()
            }
            //}
        }
        /*   IntentFilter filter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
        this.registerReceiver(broadcastReceiver, filter);*/

        /* if (Util.SDK_INT <= 23 || player == null) {
            initializePlayer();
            if (playerView != null) {
                playerView.onResume();
            }
        }*/
        //registerReceiver(broadcastReceiver,intentFilter);
    }


    override fun onPause() {
        super.onPause()
        Log.e("on", "pause")
        // unregisterReceiver(broadcastReceiver);
        //  stopService(serviceIntent);
    }

    /* @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            if (playerView != null) {
                playerView.onPause();
            }
            releasePlayer();
        }
    }*/

    /* @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            if (playerView != null) {
                playerView.onPause();
            }
            releasePlayer();
        }
    }*/
//    private fun getPlaybackLastDuration() {
////        String ss = WsConstants.DOMAIN_URL+WsConstants.GetMovieListingByCategory+CategoryId+"/"+PageCount+"/"+Limit;
//        val userid = if (AppConstants.UserId != null) Integer.valueOf(AppConstants.UserId) else 0
//        // Showloader();
//        try {
//            val request: JsonObjectRequest = object : JsonObjectRequest(
//                Request.Method.GET,
//                WsConstants.DOMAIN_URL + WsConstants.GetMovieDetail.toString() + "/" + contentId.toString() + "/" + userid,
//                null,
//                object : Listener<JSONObject?>() {
//                    fun onResponse(response: JSONObject?) {
//                        //HideLoader();
//                        if (response != null) {
//                            //String msg = null;
//                            try {
//                                val responseModel: MovieDetailResponseModel =
//                                    PageAndListRowActivity.getMapper().readValue(
//                                        response.toString(),
//                                        MovieDetailResponseModel::class.java
//                                    )
//                                if (responseModel.getStatusCode() != null && responseModel.getStatusCode()
//                                        .equals("200")
//                                ) {
//
//                                    //successresponseModel.getStatus()
//                                    val model: MovieDetailResponseDataModel =
//                                        responseModel.getMovieDatamodel()
//                                    //                                rating = Float.valueOf(0);
//                                    val all: ArrayList<MovieDetailModel> = model.getMoviedetail()
//                                    if (all != null && all.size > 0) {
//                                        movie = all[0]
//
////                                    isLive = false;
////                                    setMovieData();
//                                    }
//                                    val seconds: Int = movie.getWatchDuration().toInt()
//                                    if (seconds != 0) {
//                                        startPosition = (seconds * 1000).toLong()
//                                        //                Toast.makeText(this, ""+startPosition, Toast.LENGTH_SHORT).show();
//                                        haveStartPosition = true
//                                    }
//                                    if (movie.getPpv().equalsIgnoreCase("y")) {
//                                        initializePlayer()
//                                        if (playerView != null) {
//                                            playerView!!.onResume()
//                                        }
//                                    } else {
//                                        val i = Intent(
//                                            this@ExoplayerActivity,
//                                            DashboardActivity::class.java
//                                        )
//                                        i.flags =
//                                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
//                                        startActivity(i)
//                                    }
//                                } else {
//                                    val model: MovieDetailResponseDataModel =
//                                        responseModel.getMovieDatamodel()
//                                    //Toast.makeText(context, "" + model.getMessage(), Toast.LENGTH_SHORT).show();
//                                    initializePlayer()
//                                    if (playerView != null) {
//                                        playerView!!.onResume()
//                                    }
//                                }
//                            } catch (e: JsonMappingException) {
//                                e.printStackTrace()
//                                initializePlayer()
//                                if (playerView != null) {
//                                    playerView!!.onResume()
//                                }
//                            } catch (e: JsonProcessingException) {
//                                e.printStackTrace()
//                                initializePlayer()
//                                if (playerView != null) {
//                                    playerView!!.onResume()
//                                }
//                            }
//                        }
//                    }
//                },
//                object : ErrorListener() {
//                    fun onErrorResponse(error: VolleyError?) {
//                        //HideLoader();
//                        //Toast.makeText(context, "" + error.toString(), Toast.LENGTH_SHORT).show();
//                        initializePlayer()
//                        if (playerView != null) {
//                            playerView!!.onResume()
//                        }
//                    }
//                }) {
//                // params.put("X-version", WsConstants.API_VERSION);
//                //This is for Headers If You Needed
//                @get:Throws(AuthFailureError::class)
//                val headers: Map<String, String>?
//                    get() {
//                        val params: MutableMap<String, String> = HashMap()
//                        params["Content-Type"] = "application/json"
//                        params["X-API-Key"] = WsConstants.xApiKey
//                        // params.put("X-version", WsConstants.API_VERSION);
//                        params["X-version"] = "1.3"
//                        return params
//                    }
//            }
//            val socketTimeout = 10000
//            val policy: RetryPolicy = DefaultRetryPolicy(
//                socketTimeout,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//            )
//            request.setRetryPolicy(policy)
//            queue.add(request)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }

    override fun onStop() {
        Log.e("on", "stop")
        if (PLAYBACK_TYPE == 2 || PLAYBACK_TYPE == 3 || PLAYBACK_TYPE == 4) {
            if (!isBackPressed) {
                startPosition = Math.max(0, player!!.contentPosition)
                var seconds = (startPosition / 1000).toInt()
                if (seconds == (player!!.duration / 1000).toInt()) {
                    seconds = 0
                }
                Log.e("seconds", seconds.toString() + "")
                val addToWatchlistIntent = Intent(
                    this,
                    AddWatchHistoryService::class.java
                )
                addToWatchlistIntent.putExtra("playback", seconds)
                addToWatchlistIntent.putExtra("contentId", contentId)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Log.e("starting", "service")
                    startForegroundService(addToWatchlistIntent)
                } else {
                    Log.e("starting", "service")
                    startService(addToWatchlistIntent)
                }
            }
        }
        //if (Util.SDK_INT > 23) {
        if (playerView != null) {
            playerView!!.onPause()
        }
        releasePlayer()
        //}
        super.onStop()
    }


    override fun onDestroy() {
        super.onDestroy()
        if (timer != null) {
            timer!!.cancel()
            timer = null
        }
        stopService(serviceIntent)
        //unregisterReceiver(broadcastReceiver);
    }


    @SuppressLint("RestrictedApi")
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        Log.e("KeyCode", "" + event.keyCode)
        if (event.action == KeyEvent.ACTION_DOWN) {
            if (event.keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                playerView!!.showController()
                return true
            } else if (event.keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                playerView!!.showController()
                exo_ffwd!!.requestFocus()
                if (!isOn) {
                    player!!.pause()
                    Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
                } else {
                    if (PLAYBACK_TYPE == 1) {
                        if (player != null) player!!.seekTo(player!!.contentPosition + 15000)
                    } else {
                        if (player != null) player!!.seekTo(player!!.contentPosition + 60000)
                    }
                }
                return true
            } else if (event.keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                playerView!!.showController()
                exo_rew!!.requestFocus()
                if (!isOn) {
                    player!!.pause()
                    Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
                } else {
                    if (PLAYBACK_TYPE == 1) {
                        if (player!!.currentPosition < 15000) {
                            player!!.seekTo(0)
                        } else {
                            player!!.seekTo(player!!.contentPosition - 15000)
                        }
                    } else {
                        player!!.seekTo(player!!.contentPosition - 60000)
                    }
                }
                return true
            } else if (event.keyCode == KeyEvent.KEYCODE_DPAD_CENTER || event.keyCode == KeyEvent.KEYCODE_ENTER) {
                playerView!!.showController()
                if (ib_pause!!.getVisibility() == View.VISIBLE) {
                    ib_pause!!.requestFocus()
                    if (!isOn) {
                        player!!.pause()
                        // Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
                    } else {
                        player!!.pause()
                    }
                    playing_stage = 0
                    return true
                } else if (ib_play!!.visibility == View.VISIBLE) {
                    ib_play!!.requestFocus()
                    if (!isOn) {
                        player!!.pause()
                        // Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
                    } else {
                        player!!.play()
                    }
                    playing_stage = 1
                    return true
                }
            } else if (event.keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD || event.keyCode == KeyEvent.KEYCODE_MEDIA_STEP_FORWARD) {
                playerView!!.showController()
                exo_ffwd!!.requestFocus()
                if (!isOn) {
                    player!!.pause()
                    Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
                } else {
                    if (PLAYBACK_TYPE == 1) {
                        player!!.seekTo(player!!.contentPosition + 15000)
                    } else {
                        player!!.seekTo(player!!.contentPosition + 60000)
                    }
                }
                return true
            } else if (event.keyCode == KeyEvent.KEYCODE_MEDIA_REWIND || event.keyCode == KeyEvent.KEYCODE_MEDIA_STEP_BACKWARD) {
                playerView!!.showController()
                exo_rew!!.requestFocus()
                if (!isOn) {
                    player!!.pause()
                    Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
                } else {
                    if (PLAYBACK_TYPE == 1) {
                        if (player!!.currentPosition < 15000) {
                            player!!.seekTo(0)
                        } else {
                            player!!.seekTo(player!!.contentPosition - 15000)
                        }
                    } else {
                        player!!.seekTo(player!!.contentPosition - 60000)
                    }
                }
                return true
            } else if (event.keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE) {
                playerView!!.showController()
                playerView!!.showController()
                if (ib_pause!!.getVisibility() == View.VISIBLE) {
                    ib_pause!!.requestFocus()
                    if (!isOn) {
                        player!!.pause()
                        // Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
                    } else {
                        player!!.pause()
                    }
                    playing_stage = 0
                    return true
                } else if (ib_play!!.visibility == View.VISIBLE) {
                    ib_play!!.requestFocus()
                    if (!isOn) {
                        player!!.pause()
                        // Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
                    } else {
                        player!!.play()
                    }
                    playing_stage = 1
                    return true
                }
            }
        }
        return playerView!!.dispatchKeyEvent(event) || super.dispatchKeyEvent(event)
    }

    protected fun initializePlayer(): Boolean {
        Log.e("url", url!!)
        if (PLAYBACK_TYPE == 1 || PLAYBACK_TYPE == 2) {  // Non DRM movie or Trailer

            //HLS SOURCE
            if (player == null) {
                mediaItem = MediaItem.fromUri(url!!)
                val extractorsFactory = DefaultExtractorsFactory()
                    .setConstantBitrateSeekingEnabled(true)
                player = SimpleExoPlayer.Builder(this@ExoplayerActivity)
                    .setMediaSourceFactory(DefaultMediaSourceFactory(this, extractorsFactory))
                    .build()
                playerView!!.player = player
                val dataSourceFactory: DataSource.Factory = DefaultHttpDataSourceFactory()
                mediaSource = HlsMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(mediaItem!!)
            }
            if (haveStartPosition) {
                player!!.setSeekParameters(SeekParameters.CLOSEST_SYNC)
                player!!.seekTo(startPosition)
            }
            player!!.setMediaItem(mediaItem!!)
        } else if (PLAYBACK_TYPE == 4) {
            if (player == null) {
                mediaItem = MediaItem.fromUri(url!!)
                val extractorsFactory = DefaultExtractorsFactory()
                    .setConstantBitrateSeekingEnabled(true)
                player = SimpleExoPlayer.Builder(this@ExoplayerActivity)
                    .setMediaSourceFactory(DefaultMediaSourceFactory(this, extractorsFactory))
                    .build()
                playerView!!.player = player
                playerView!!.useController = false
                val dataSourceFactory: DataSource.Factory = DefaultHttpDataSourceFactory()
                mediaSource = HlsMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(mediaItem!!)
            }
            if (haveStartPosition) {
                player!!.setSeekParameters(SeekParameters.CLOSEST_SYNC)
                player!!.seekTo(startPosition)
            }
            player!!.setMediaItem(mediaItem!!)
        } else if (PLAYBACK_TYPE == 3) { // DRM content
            if (player == null) {
                val extractorsFactory = DefaultExtractorsFactory()
                    .setConstantBitrateSeekingEnabled(true)
                player = SimpleExoPlayer.Builder(this@ExoplayerActivity)
                    .setMediaSourceFactory(DefaultMediaSourceFactory(this, extractorsFactory))
                    .build()
                playerView!!.player = player
                var drmSessionManager: DrmSessionManager? = null
                val drmKeyRequestPropertiesList = ArrayList<String>()
                drmKeyRequestPropertiesList.add(token_key!!)
                drmKeyRequestPropertiesList.add(token_value!!)
                var keyRequestPropertiesArray: Array<String>? = null
                keyRequestPropertiesArray = drmKeyRequestPropertiesList.toTypedArray()

//        String[] keyRequestPropertiesArray = intent.getStringArrayExtra(DRM_KEY_REQUEST_PROPERTIES);
                val multiSession = false
                var errorStringId = 1
                if (Util.SDK_INT < 18) {
                    errorStringId = 2
                } else {
                    drmSessionManager = try {
                        val drmSchemeUuid = DemoUtil.getDrmUuid(drmSchemeExtra)
                        buildDrmSessionManagerV18(
                            drmSchemeUuid, drmLicenseUrl!!,
                            keyRequestPropertiesArray, multiSession
                        )


                        /*   boolean preferExtensionDecoders = false;
                        @DefaultRenderersFactory.ExtensionRendererMode int extensionRendererMode =
                                useExtensionRenderers()
                                        ? (preferExtensionDecoders ? DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER
                                        : DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON)
                                        : DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF;


                            MediaSourceFactory mediaSourceFactory =
                                    new DefaultMediaSourceFactory(ExoplayerActivity.this);*/

                        /*drmSessionManager = buildDrmSessionManagerV18(drmSchemeUuid, drmLicenseUrl,
                                    null, multiSession);*/
                    } catch (e: UnsupportedDrmException) {
                        Toast.makeText(this, "" + e.toString(), Toast.LENGTH_LONG).show()
                        return false

                        //                        errorStringId = 3;
                    }
                }
                if (drmSessionManager == null) {
                    Toast.makeText(this, "errorna1", Toast.LENGTH_SHORT).show()
                }
                mediaItem = MediaItem.fromUri(url!!)
                val dataSourceFactory: DataSource.Factory = DefaultHttpDataSourceFactory()
                mediaSource = DashMediaSource.Factory(dataSourceFactory)
                    .setDrmSessionManager(drmSessionManager)
                    .createMediaSource(mediaItem!!)
            }
            player!!.setMediaSource(mediaSource!!)
            if (haveStartPosition) {
                player!!.seekTo(startWindow, startPosition)
            }
        } else if (PLAYBACK_TYPE == 4) {
            if (player == null) {
                mediaItem = MediaItem.fromUri(url!!)
                player = SimpleExoPlayer.Builder(this@ExoplayerActivity).build()
                player!!.addListener(object : Player.EventListener {
                    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                        if (playbackState == Player.STATE_IDLE ||
                            !playWhenReady
                        ) {
                            playerView!!.keepScreenOn = false
                        } else if (playbackState == Player.STATE_ENDED) {
                            if (broadcastReceiver.isOrderedBroadcast) {
                                LocalBroadcastManager.getInstance(this@ExoplayerActivity)
                                    .unregisterReceiver(broadcastReceiver)
                            }
                            finish()
                        } else { // STATE_IDLE, STATE_ENDED
                            // This prevents the screen from getting dim/lock
                            if (!durationSet) {
                                realDurationMillis = player!!.duration
                                Log.e("set duration", "" + realDurationMillis)
                                durationSet = true
                            }
                            playerView!!.keepScreenOn = true
                        }
                    }

                    override fun onPlayerError(error: ExoPlaybackException) {
                        if (player != null) {
                            player!!.pause()
                        }
                    }
                })
                playerView!!.player = player
                playerView!!.useController = true
                playerView!!.controllerShowTimeoutMs = 0
                //   playerView.setControllerHideOnTouch(false);
                val dataSourceFactory: DataSource.Factory = DefaultHttpDataSourceFactory()
                mediaSource = HlsMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(mediaItem!!)
            }
            if (isFestivalMovie) {
                Log.e("is", "festival")
                // TODO : timer call : Polling
                val delay = timeInterval * 1000 // delay for 0 sec.
                val period = timeInterval * 1000
                if (timer == null) {
                    timer = Timer()
                    timer!!.scheduleAtFixedRate(object : TimerTask() {
                        override fun run() {
                            //fetchContentUrl()
                        }
                    }, delay.toLong(), period.toLong())
                }
            }
            if (haveStartPosition) {
                player!!.seekTo(startWindow, startPosition)
            }
            player!!.setMediaItem(mediaItem!!)
        }

        //progressive
        /* DataSource.Factory dataSourceFactory = new DefaultHttpDataSourceFactory();
            mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(mediaItem);*/

        // Dash Source
        // Create a data source factory.


//             DrmSessionManager sessionManager = new DefaultDrmSessionManager()
//            player.addListener(new PlayerEventListener());
//            player.addAnalyticsListener(new EventLogger(trackSelector));
//            player.setAudioAttributes(AudioAttributes.DEFAULT, true);
//            player.setPlayWhenReady(startAutoPlay);
//            playerView.setPlaybackPreparer(this);
//            debugViewHelper = new DebugTextViewHelper(player, debugTextView);
//            debugViewHelper.start();
//        }


//        DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(this,
//                drmSessionManager, extensionRendererMode);

//        DefaultRenderersFactory renderersFactory1 = new DefaultRenderersFactory()


//        boolean haveStartPosition = startWindow != C.INDEX_UNSET;

//        MediaSource mediaSources = buildMediaSource("0");
// Set the media item to be played.
        player!!.prepare()
        player!!.play()
        progress_circular!!.visibility = View.VISIBLE
        player!!.addListener(object : Player.EventListener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if (playbackState == Player.STATE_BUFFERING) {
                    progress_circular!!.visibility = View.VISIBLE
                    //playerView.hideController();
                    Log.e("buffer", "" + player!!.bufferedPosition)
                    playerView!!.keepScreenOn = false
                } else if (playbackState == Player.STATE_IDLE || !playWhenReady) {
                    progress_circular!!.visibility = View.GONE
                    //playerView.showController();
                    playerView!!.keepScreenOn = false
                } else if (playbackState == Player.STATE_ENDED) {
                    if (broadcastReceiver.isOrderedBroadcast) {
                        LocalBroadcastManager.getInstance(this@ExoplayerActivity)
                            .unregisterReceiver(broadcastReceiver)
                    }
                    onBackPressed()
                    progress_circular!!.visibility = View.GONE
                    //playerView.showController();
                } else { // STATE_IDLE, STATE_ENDED
                    // This prevents the screen from getting dim/lock
                    progress_circular!!.visibility = View.GONE
                    playerView!!.keepScreenOn = true

                    //playerView.showController();
                }
            }
        })
        playerView?.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
//        player.setVideoS4calingMode(Renderer.VIDEO_SCALING_MODE_SCALE_TO_FIT);
//        updateButtonVisibility();
        startService(serviceIntent)
        return true
    }


//    fun useExtensionRenderers(): Boolean {
//        return ExoplayerConstants.FLAVOR.equals("withExtensions")
//    }


    protected fun releasePlayer() {
        if (player != null) {
            player!!.release()
            player = null
        }
    }

/*    private DrmSessionManager buildDrmSessionManagerV18(UUID uuid, String licenseUrl, String[] keyRequestPropertiesArray, boolean multiSession)
            throws UnsupportedDrmException {
        HttpMediaDrmCallback drmCallback = new HttpMediaDrmCallback(licenseUrl,
                buildHttpDataSourceFactory(false));
        if (keyRequestPropertiesArray != null) {
            for (int i = 0; i < keyRequestPropertiesArray.length - 1; i += 2) {
                drmCallback.setKeyRequestProperty(keyRequestPropertiesArray[i],
                        keyRequestPropertiesArray[i + 1]);
            }
        }
        return new DefaultDrmSessionManager(uuid, FrameworkMediaDrm.newInstance(uuid), drmCallback,
                null, mainHandler, null, multiSession);
    }*/

    /*    private DrmSessionManager buildDrmSessionManagerV18(UUID uuid, String licenseUrl, String[] keyRequestPropertiesArray, boolean multiSession)
            throws UnsupportedDrmException {
        HttpMediaDrmCallback drmCallback = new HttpMediaDrmCallback(licenseUrl,
                buildHttpDataSourceFactory(false));
        if (keyRequestPropertiesArray != null) {
            for (int i = 0; i < keyRequestPropertiesArray.length - 1; i += 2) {
                drmCallback.setKeyRequestProperty(keyRequestPropertiesArray[i],
                        keyRequestPropertiesArray[i + 1]);
            }
        }
        return new DefaultDrmSessionManager(uuid, FrameworkMediaDrm.newInstance(uuid), drmCallback,
                null, mainHandler, null, multiSession);
    }*/
    @Throws(UnsupportedDrmException::class)
    private fun buildDrmSessionManagerV18(
        uuid: UUID,
        licenseUrl: String,
        keyRequestPropertiesArray: Array<String>?,
        multiSession: Boolean
    ): DrmSessionManager? {
        val drmCallback = HttpMediaDrmCallback(
            licenseUrl,
            buildHttpDataSourceFactory(DefaultBandwidthMeter())!!
        )
        if (keyRequestPropertiesArray != null) {
            var i = 0
            while (i < keyRequestPropertiesArray.size - 1) {
                drmCallback.setKeyRequestProperty(
                    keyRequestPropertiesArray[i],
                    keyRequestPropertiesArray[i + 1]
                )
                i += 2
            }
        }
        return DefaultDrmSessionManager(
            uuid, FrameworkMediaDrm.newInstance(uuid), drmCallback,
            null, multiSession
        )
    }

/*
    private MediaSource buildMediaSource(String type) {

        switch (Integer.valueOf(type)) {
            case C.TYPE_DASH:
                return new DashMediaSource.Factory(
                        new DefaultDashChunkSource.Factory(mediaDataSourceFactory),
                        buildHttpDataSourceFactory(false))
                        .createMediaSource(mediaItem);
            case C.TYPE_SS:
                return new SsMediaSource.Factory(
                        new DefaultSsChunkSource.Factory(mediaDataSourceFactory),
                        buildHttpDataSourceFactory(false))
                        .createMediaSource(mediaItem);
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(mediaDataSourceFactory)
                        .createMediaSource(mediaItem);
            case C.TYPE_OTHER:
                return new ExtractorMediaSource.Factory(mediaDataSourceFactory)
                        .createMediaSource(mediaItem);
            default: {
                throw new IllegalStateException("Unsupported type: " + type);
            }
        }
    }
*/

    /*
    private MediaSource buildMediaSource(String type) {

        switch (Integer.valueOf(type)) {
            case C.TYPE_DASH:
                return new DashMediaSource.Factory(
                        new DefaultDashChunkSource.Factory(mediaDataSourceFactory),
                        buildHttpDataSourceFactory(false))
                        .createMediaSource(mediaItem);
            case C.TYPE_SS:
                return new SsMediaSource.Factory(
                        new DefaultSsChunkSource.Factory(mediaDataSourceFactory),
                        buildHttpDataSourceFactory(false))
                        .createMediaSource(mediaItem);
            case C.TYPE_HLS:
                return new HlsMediaSource.Factory(mediaDataSourceFactory)
                        .createMediaSource(mediaItem);
            case C.TYPE_OTHER:
                return new ExtractorMediaSource.Factory(mediaDataSourceFactory)
                        .createMediaSource(mediaItem);
            default: {
                throw new IllegalStateException("Unsupported type: " + type);
            }
        }
    }
*/
    private fun updateStartPosition() {
        if (player != null) {
            startAutoPlay = player!!.playWhenReady
            startWindow = player!!.currentWindowIndex
            startPosition = Math.max(0, player!!.contentPosition)
        }
    }

    protected fun clearStartPosition() {
        startAutoPlay = true
        startWindow = C.INDEX_UNSET
        startPosition = C.TIME_UNSET
    }

//    fun fetchContentUrl() {
//        Log.e("interval  $timeInterval", Calendar.getInstance()[Calendar.SECOND].toString())
//        val request: JsonObjectRequest = object : JsonObjectRequest(
//            Request.Method.GET,
//            WsConstants.DOMAIN_URL + WsConstants.getContentVideoLink + contentId,
//            null,
//            object : Listener<JSONObject?>() {
//                fun onResponse(response: JSONObject?) {
//                    if (response != null) {
//                        try {
//                            val statusCode = response.getString("statusCode")
//                            if (statusCode == "200") {
//                                val data = response.getJSONObject("data")
//                                val dataArray = data.getJSONArray("contentdetail")
//                                if (dataArray.length() > 0) {
//                                    val contentObject = dataArray.getJSONObject(0)
//                                    Log.e(
//                                        "interval  $timeInterval",
//                                        contentObject.getString("videoPath")
//                                    )
//                                    if (url != contentObject.getString("videoPath")) {
//                                        url = contentObject.getString("videoPath")
//                                        if (playerView != null) {
//                                            playerView!!.onPause()
//                                        }
//                                        releasePlayer()
//                                        initializePlayer()
//                                        if (playerView != null) {
//                                            playerView!!.onResume()
//                                        }
//                                    }
//                                }
//                            }
//                        } catch (e: JSONException) {
//                            e.printStackTrace()
//                        }
//                    }
//                }
//            },
//            object : ErrorListener() {
//                fun onErrorResponse(error: VolleyError) {
//                    /* if (error instanceof TimeoutError) {
//                    Toast.makeText(ExoplayerActivity.this, R.string.timeout_message, Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (error.getCause() instanceof IOException) {
//                    Toast.makeText(ExoplayerActivity.this, getString(R.string.internet_not_available), Toast.LENGTH_SHORT).show();
//                    return;
//                }*/
//                    Toast.makeText(
//                        this@ExoplayerActivity,
//                        "" + error.toString(),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }) {
//            //This is for Headers If You Needed
//            val headers: Map<String, String>?
//                get() {
//                    val params: MutableMap<String, String> = HashMap()
//                    params["Content-Type"] = "application/json"
//                    params["X-API-Key"] = WsConstants.xApiKey
//                    return params
//                }
//        }
//        val policy: RetryPolicy = DefaultRetryPolicy(
//            10000,
//            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
//        )
//        request.setRetryPolicy(policy)
//        queue.add(request)
//    }

    fun resumeTrack() {
        if (player!!.playbackError != null) {
            player!!.retry()
        }
        Log.e("buffered", "" + player!!.bufferedPosition)
        Log.e("played", "" + player!!.currentPosition)
        Log.e("duration", "" + player!!.duration)
        player!!.playWhenReady = true
        player!!.play()
    }

    companion object{
        fun createIntentExoplayer(context: Context?, dashUrl : String,licenceUrl:String,token: String,watchDuration : Int, contentId:Int): Intent {
            val intent = Intent(context, ExoplayerActivity::class.java)
            intent.putExtra("DashUrl", dashUrl)
            intent.putExtra("licenceUrl" ,licenceUrl)
            intent.putExtra("token",token)
            intent.putExtra("WatchDuration", watchDuration)
            intent.putExtra("contentId" , contentId)
            return intent
        }
        fun exoplayerMovieContent(context: Context?, movieUrl : String) : Intent{
            val intent = Intent(context, ExoplayerActivity::class.java)
            intent.putExtra("movieUrl", movieUrl)

            return intent
        }
    }

}


