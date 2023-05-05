package `in`.fiberstory.tfsplaytv.activities

import `in`.fiberstory.tfsplaytv.R
import `in`.fiberstory.tfsplaytv.adapter.KeypadAdapter
import `in`.fiberstory.tfsplaytv.fragments.SearchFragment
import `in`.fiberstory.tfsplaytv.interfaces.NavigationMenuCallback
import androidx.fragment.app.FragmentActivity

//import `in`.fiberstory.tfsplaytv.R
//import `in`.fiberstory.tfsplaytv.adapter.KeypadAdapter
//import `in`.fiberstory.tfsplaytv.fragments.SearchFragment
//import android.Manifest
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.content.res.ColorStateList
//import android.graphics.Color
//import android.graphics.drawable.ColorDrawable
//import android.os.Build
//import android.os.Bundle
//import android.speech.RecognitionListener
//import android.speech.RecognizerIntent
//import android.speech.SpeechRecognizer
//import android.text.Editable
//import android.text.TextWatcher
//import android.util.Log
//import android.view.Gravity
//import android.view.View
//import android.view.ViewGroup
//import android.view.animation.Animation
//import android.view.animation.AnimationUtils
//import android.widget.*
//import androidx.annotation.RequiresApi
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import androidx.fragment.app.FragmentActivity
//import androidx.leanback.widget.VerticalGridView
//import androidx.recyclerview.widget.GridLayoutManager
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import java.lang.reflect.InvocationTargetException
//import java.util.*

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.leanback.widget.VerticalGridView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.reflect.InvocationTargetException
import java.util.ArrayList
import java.util.Locale


class SearchActivity : FragmentActivity(), View.OnFocusChangeListener, View.OnClickListener {
    private var mRecyclerView: RecyclerView? = null
    var mVerticalGridView: VerticalGridView? = null
    var mEditText: EditText? = null
    var rgKeyboardType: RadioGroup? = null
    private var rbABC: RadioButton? = null
    private var rbabc: RadioButton? = null
    private var rb123: RadioButton? = null
    var stringBuilderValue = StringBuilder()
    var mLoading: ProgressBar? = null
    var imgSpace: ImageView? = null
    var imgBackspace: ImageView? = null
    private var imgVoice: ImageView? = null
    private var speechRecognizer: SpeechRecognizer? = null
    private var speechRecognizerIntent: Intent? = null
    private var animation: Animation? = null
    private var isRecognitionAvailable = false
    private lateinit var mSearchFragment: SearchFragment
    private var filterType = "title"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        Init()
        configureKeyboard()
        SearchFragment.flagPosition = true
        if (savedInstanceState == null) {
            mSearchFragment = SearchFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.search_container, mSearchFragment).commit()
        }
    }

    private fun configureKeyboard() {
        setKeypadData("CAPITAL")
        rgKeyboardType?.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->
            val selectedId: Int = radioGroup.checkedRadioButtonId
            when (selectedId) {
                rbABC?.id -> {
                    setKeypadData("CAPITAL")
                }
                rbabc?.id -> {
                    setKeypadData("SMALL")
                }
                else -> {
                    setKeypadData("NUMBER")
                }
            }
        })
    }

    override fun onSearchRequested(): Boolean {
        return super.onSearchRequested()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setKeypadData(type: String) {
        val stringBuilder = StringBuilder()
        when {
            type.equals("CAPITAL", ignoreCase = true) -> {
                var i = 'A'
                while (i <= 'Z') {
                    stringBuilder.append(i).append(",")
                    i++
                }
            }
            type.equals("SMALL", ignoreCase = true) -> {
                var i = 'a'
                while (i <= 'z') {
                    stringBuilder.append(i).append(",")
                    i++
                }
            }
            type.equals("NUMBER", ignoreCase = true) -> {
                var i = '0'
                while (i <= '9') {
                    stringBuilder.append(i).append(",")
                    i++
                }
            }
        }
        if (stringBuilder.isNotEmpty()) {
            stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","))
        }
        val keyList: List<String> =
            ArrayList(listOf(*stringBuilder.toString().split(",".toRegex()).toTypedArray()))
        val keyPadAdapter = KeypadAdapter(this@SearchActivity, keyList)
        mRecyclerView?.adapter = keyPadAdapter
        keyPadAdapter.notifyDataSetChanged()
        keyPadAdapter.setOnItemClickListener(object : KeypadAdapter.OnItemClickListener {
            override fun onItemClick(view: View?, position: Int) {
                stringBuilderValue.append(keyList[position])
                mEditText?.setText(stringBuilderValue.toString())
                mEditText?.addTextChangedListener(object : TextWatcher {
                    override fun onTextChanged(
                        s: CharSequence, start: Int, before: Int, count: Int
                    ) {
                    }

                    override fun beforeTextChanged(
                        s: CharSequence, start: Int, count: Int, after: Int
                    ) {
                    }

                    override fun afterTextChanged(s: Editable) {
                        Log.d("searchResult ", "afterTextChanged: " + s.toString())
                        mSearchFragment.getSearchResult(s.toString())
                    }
                })
            }
        })
        imgSpace?.onFocusChangeListener = this
        imgBackspace?.onFocusChangeListener = this
        imgVoice?.onFocusChangeListener = this
        linearSearchBy?.onFocusChangeListener = this
        imgSpace?.setOnClickListener(this)
        imgBackspace?.setOnClickListener(this)
        imgVoice?.setOnClickListener(this)
        linearSearchBy?.setOnClickListener(this)
    }

    private fun Init() {
        animation = AnimationUtils.loadAnimation(this@SearchActivity, R.anim.wobble)
        mRecyclerView = findViewById(R.id.recyclerViewKP)
        mLoading = findViewById(R.id.mLoading)
        rgKeyboardType = findViewById(R.id.rgKeyboardType)
        rbABC = findViewById(R.id.rbABC)
        rbabc = findViewById(R.id.rbabc)
        rb123 = findViewById(R.id.rb123)
        val lm = GridLayoutManager(this@SearchActivity, 1)
        lm.orientation = LinearLayoutManager.HORIZONTAL
        with(mRecyclerView) {
//            lm.orientation = LinearLayoutManager.HORIZONTAL
            this?.layoutManager = GridLayoutManager(this@SearchActivity,7)
//            this?.layoutManager = lm
            this?.setHasFixedSize(true)
        }
        mEditText = findViewById(R.id.editText)
        txtSelectedFilter = findViewById(R.id.txtSelectedFilter)
        txtErrorMessage = findViewById(R.id.errorMsg)
        imgSpace = findViewById(R.id.imgSpace)
        linearSearchBy = findViewById(R.id.linearSearchBy)
        searchNotFound = findViewById(R.id.searchNotFound)
        imgBackspace = findViewById(R.id.imgBackspace)
        imgVoice = findViewById(R.id.imgVoice)

        // check whether device is supported for speech recognition or not  if device not supported then hide speek icon
//        isRecognitionAvailable = SpeechRecognizer.isRecognitionAvailable(this)
        imgVoice.let {
            it?.visibility = View.GONE
        }
        if (isRecognitionAvailable) {
            imgVoice.let {
                it?.visibility = View.VISIBLE
            }
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
            speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            speechRecognizerIntent!!.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            speechRecognizerIntent!!.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            speechRecognizer?.setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(bundle: Bundle) {
                    imgVoice.let {
                        it?.animation = animation
                    }
                }

                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(v: Float) {}
                override fun onBufferReceived(bytes: ByteArray) {}
                override fun onEndOfSpeech() {
                    imgVoice.let {
                        it?.clearAnimation()
                    }
                }

                override fun onError(i: Int) {
                    Log.d("TAG", "onError: $i")
                }

                override fun onResults(bundle: Bundle) {
                    stringBuilderValue.append(
                        bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.get(0)
                    )
                    mEditText?.setText(stringBuilderValue.toString())
                    mSearchFragment.getSearchResult(stringBuilderValue.toString())
                }

                override fun onPartialResults(bundle: Bundle) {}
                override fun onEvent(i: Int, bundle: Bundle) {
                    Log.d("TAG", "onEvent: $i")
                }
            })
        }
    }

    override fun onBackPressed() {
        if(SearchFragment.flagPosition) {
            super.onBackPressed()

            finish()

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }


    protected override fun onDestroy() {
        super.onDestroy()
        if (speechRecognizer != null) speechRecognizer?.destroy()
    }

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        when (v.id) {
            R.id.imgSpace -> {
                val imgSpace: ImageView = findViewById(R.id.imgSpace)
                if (hasFocus) {
                    imgSpace.imageTintList = ColorStateList.valueOf(Color.BLACK)
                } else {
                    imgSpace.imageTintList = ColorStateList.valueOf(Color.WHITE)
                }
            }
            R.id.imgBackspace -> {
                val imgBackspace: ImageView = findViewById(R.id.imgBackspace)
                if (hasFocus) {
                    imgBackspace.imageTintList = ColorStateList.valueOf(Color.BLACK)
                } else {
                    imgBackspace.imageTintList = ColorStateList.valueOf(Color.WHITE)
                }
            }
            R.id.imgVoice -> if (hasFocus) {
                imgVoice!!.setBackgroundResource(R.drawable.circle_normal)
            } else {
                imgVoice!!.setBackgroundResource(R.color.colorTransparent)
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.linearSearchBy -> {
                val popupView: View = layoutInflater.inflate(R.layout.pop_layout, null)
                val popupWindow = PopupWindow(
                    popupView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    true
                )
                val txtTitle: TextView = popupView.findViewById<TextView>(R.id.txt1)
                val txtGenre: TextView = popupView.findViewById<TextView>(R.id.txt2)
                val txtPeople: TextView = popupView.findViewById<TextView>(R.id.txt3)
                txtTitle.setOnClickListener(View.OnClickListener {
                    filterType = "title"
                    txtSelectedFilter?.text = "Title"
                    mEditText?.hint = "eg. Bahubali, CID"
                    stringBuilderValue.setLength(0)
                    mEditText?.setText(stringBuilderValue.toString())
                    popupWindow.dismiss()
                })
                txtGenre.setOnClickListener(View.OnClickListener {
                    filterType = "genre"
                    txtSelectedFilter?.text = "Genre"
                    mEditText?.hint = "eg. comedy, drama, horror etc."
                    stringBuilderValue.setLength(0)
                    mEditText?.setText(stringBuilderValue.toString())
                    popupWindow.dismiss()
                })
                txtPeople.setOnClickListener(View.OnClickListener {
                    filterType = "people"
                    txtSelectedFilter?.text = "People"
                    mEditText?.hint = "eg. hrithik, priyanka, karan etc."
                    stringBuilderValue.setLength(0)
                    mEditText?.setText(stringBuilderValue.toString())
                    popupWindow.dismiss()
                })
                popupWindow.isFocusable = true
                popupWindow.setBackgroundDrawable(ColorDrawable())
                val location = IntArray(2)
                v.getLocationOnScreen(location)
                popupWindow.showAtLocation(
                    v, Gravity.NO_GRAVITY, location[0], location[1] + v.height
                )
            }
            R.id.imgSpace -> {
                mEditText?.setText(stringBuilderValue.append(" "))
                mSearchFragment.getSearchResult(stringBuilderValue.toString())
            }
            R.id.imgBackspace -> {
                if (stringBuilderValue.isEmpty() || stringBuilderValue.length == 1) {
                    mSearchFragment.movieAdded = false
                    mSearchFragment.showsAdded = false
                    mSearchFragment.channelAdded = false
                }
                if (stringBuilderValue.isNotEmpty()) {
                    mEditText?.setText(stringBuilderValue.deleteCharAt(stringBuilderValue.length - 1))
                }
                mSearchFragment.getSearchResult(stringBuilderValue.toString())
            }
            R.id.imgVoice -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkPermission()
            } else {
                configureVoice()
            }
        }
    }

    private fun configureVoice() {
        stringBuilderValue.setLength(0)
        mEditText?.setText(stringBuilderValue.toString())
        speechRecognizer?.startListening(speechRecognizerIntent)
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this@SearchActivity,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@SearchActivity, Manifest.permission.RECORD_AUDIO
                )
            ) {
                requestPermissions(
                    arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_ID_MULTIPLE_PERMISSIONS
                )
            } else {
                requestPermissions(
                    arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_ID_MULTIPLE_PERMISSIONS
                )
            }
        } else {
            configureVoice()
        }
    }

    @JvmName("onRequestPermissionsResult1")
    @RequiresApi(api = Build.VERSION_CODES.M)
    fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>?, grantResults: IntArray
    ) {
        if (permissions != null) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> if (grantResults.isNotEmpty()) {
                val readExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (readExternalFile) {
                    configureVoice()
                } else {
                    requestPermissions(
                        arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_ID_MULTIPLE_PERMISSIONS
                    )
                }
            }
        }
    }

    private fun setAssistBlocked(view: View, blocked: Boolean) {
        try {
            val setAssistBlockedMethod =
                View::class.java.getMethod("setAssistBlocked", Boolean::class.javaPrimitiveType)
            setAssistBlockedMethod.invoke(view, blocked)
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }

    companion object {
        private val TAG = SearchActivity::class.java.simpleName
        var linearSearchBy: LinearLayout? = null
        var searchNotFound: LinearLayout? = null
        var txtSelectedFilter: TextView? = null
        var txtErrorMessage: TextView? = null
        private const val REQUEST_ID_MULTIPLE_PERMISSIONS = 1
    }
}