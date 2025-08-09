package com.example.propertyvaluepredictor

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import java.nio.FloatBuffer

class MainActivity : AppCompatActivity() {

    private lateinit var houseTypeDropdown: AutoCompleteTextView
    private val houseTypes = arrayOf("Condo", "Townhouse", "Detached")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val inputArea = findViewById<TextInputEditText>(R.id.AreaInput)
        val inputRooms = findViewById<TextInputEditText>(R.id.RoomInput)

        // Setup dropdown
        val adapter = ArrayAdapter(this, R.layout.list_item, houseTypes)
        houseTypeDropdown = findViewById(R.id.house_type_auto_complete_text_view)
        houseTypeDropdown.setAdapter(adapter)

        val predictBtn = findViewById<Button>(R.id.calculate_button)
        val resultText = findViewById<TextView>(R.id.output_text)

        predictBtn.setOnClickListener {
            val areaData = inputArea.text.toString().toFloatOrNull()
            val roomData = inputRooms.text.toString().toFloatOrNull()
            val selectedHouseType = houseTypeDropdown.text?.toString()

            val houseTypeData: Int = when (selectedHouseType) {
                "Condo" -> 0
                "Townhouse" -> 1
                "Detached" -> 2
                else -> 0
            }

            if (areaData != null && roomData != null) {
                val ortEnvironment = OrtEnvironment.getEnvironment()
                val ortSession = createORTSession(ortEnvironment)
                val output = ortSession?.let {
                    executeModel(areaData, roomData, houseTypeData, it, ortEnvironment)
                }
                resultText.text = getString(R.string.predicted_price_placeholder, "$output $")
            } else {
                Toast.makeText(this, "Please input the area and room data", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Save dropdown text before rotation
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("selectedHouseType", houseTypeDropdown.text.toString())
    }

    // Restore dropdown text after rotation
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val adapter = ArrayAdapter(this, R.layout.list_item, houseTypes)
        houseTypeDropdown.setAdapter(adapter)

        val savedValue = savedInstanceState.getString("selectedHouseType", "")
        houseTypeDropdown.setText(savedValue, false) // false avoids triggering filtering
    }

    private fun executeModel(
        areaData: Float,
        roomData: Float,
        houseTypeData: Int,
        ortSession: OrtSession,
        ortEnvironment: OrtEnvironment?
    ): Float {
        val inputName = ortSession.inputNames?.iterator()?.next()
        val floatBufferInput =
            FloatBuffer.wrap(floatArrayOf(areaData, roomData, houseTypeData.toFloat()))

        OnnxTensor.createTensor(ortEnvironment, floatBufferInput, longArrayOf(1, 3)).use { tensorInput ->
            ortSession.run(mapOf(inputName to tensorInput)).use { result ->
                @Suppress("UNCHECKED_CAST")
                val output = result[0].value as Array<FloatArray>
                return output[0][0]
            }
        }
    }

    private fun createORTSession(ortEnvironment: OrtEnvironment?): OrtSession? {
        return try {
            resources.openRawResource(R.raw.house_price_model).use { modelInputStream ->
                val modelBytes = modelInputStream.readBytes()
                ortEnvironment?.createSession(modelBytes)
            }
        } catch (e: Exception) {
            System.err.println("DEBUG: Error loading ONNX model or creating session.")
            e.printStackTrace()
            Toast.makeText(
                this,
                "Error loading model or creating session: ${e.localizedMessage}",
                Toast.LENGTH_LONG
            ).show()
            null
        }
    }
}