package `in`.fiberstory.tfsplaytv.fragments

import `in`.fiberstory.tfsplaytv.R
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.leanback.app.BrowseSupportFragment


class ContactUSFragment : BrowseSupportFragment() {
    private var txtQuery: TextView? = null
    private var txtQueryLimit: TextView? = null
    private var edtQuery: EditText? = null
    private var btnSubmit: Button? = null
    private var selectedQueryID = ""
    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_contactus, container, false)
    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(false)
        Init(v)
        setData()
    }

    private fun setData() {
        edtQuery!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
                val tick = start + after
                if (tick < 1000) {
                    val remaining = 1000 - tick
                    txtQueryLimit!!.text = "Character Limit of $remaining"
                }
            }

            override fun onTextChanged(
                charSequence: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        txtQuery!!.setOnClickListener {

        }
        btnSubmit!!.setOnClickListener {
            if (selectedQueryID.equals("", ignoreCase = true)) {
                Toast.makeText(getActivity(), "Please select category!", Toast.LENGTH_SHORT).show()
            } else if (edtQuery!!.text.toString().equals("", ignoreCase = true)) {
                Toast.makeText(getActivity(), "please enter description!", Toast.LENGTH_SHORT)
                    .show()
            } else {
            }
        }
    }

    fun escapeMetaCharacters(inputString: String): String {
        var inputString = inputString
        val metaCharacters = arrayOf(
            "\\",
            "^",
            "$",
            "{",
            "}",
            "[",
            "]",
            "(",
            ")",
            ".",
            "*",
            "+",
            "?",
            "|",
            "<",
            ">",
            "-",
            "&",
            "%",
            "'"
        )
        for (i in metaCharacters.indices) {
            if (inputString.contains(metaCharacters[i])) {
                inputString = inputString.replace(metaCharacters[i], "\\" + metaCharacters[i])
            }
        }
        return inputString
    }

    private fun Init(v: View) {
        edtQuery = v.findViewById(R.id.edtQuery)
        txtQuery = v.findViewById(R.id.txtQuery)
        txtQueryLimit = v.findViewById(R.id.txtQueryLimit)
        btnSubmit = v.findViewById(R.id.btnSubmit)
    }
}