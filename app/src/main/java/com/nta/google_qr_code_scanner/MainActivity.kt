package com.nta.google_qr_code_scanner

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.alexzhirkevich.customqrgenerator.QrData
import com.github.alexzhirkevich.customqrgenerator.style.Color
import com.github.alexzhirkevich.customqrgenerator.vector.QrCodeDrawable
import com.github.alexzhirkevich.customqrgenerator.vector.QrVectorOptions
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorBackground
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorBallShape
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorColor
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorColors
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorFrameShape
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorLogo
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorLogoPadding
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorLogoShape
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorPixelShape
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorShapes
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.nta.google_qr_code_scanner.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.mbGenerateQrCode.setOnClickListener {
            if (binding.etQrCodeValue.text.toString().isNotEmpty())
                generateQrCode()
            else
                Toast.makeText(this, "Please enter a value", Toast.LENGTH_SHORT).show()
        }

        binding.mbScanQrCode.setOnClickListener {
            scanQrCode()
        }

    }

    private fun scanQrCode() {
        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE,
                Barcode.FORMAT_AZTEC)
            .enableAutoZoom()
            .build()

        val scanner = GmsBarcodeScanning.getClient(this, options)

        scanner.startScan()
            .addOnSuccessListener { barcode ->
                val result = barcode.rawValue
                binding.tvScannedValue.text = "Scanned Value : "+result
            }
            .addOnCanceledListener {

            }
            .addOnFailureListener {

            }
    }

    private fun generateQrCode() {
        val data = QrData.Text(binding.etQrCodeValue.text.toString())

        val options = QrVectorOptions.Builder()
            .setPadding(.1f)
            .setLogo(
                QrVectorLogo(
                    drawable = ContextCompat.getDrawable(this, R.drawable.ic_qr_logo),
                    size = .27f,
                    padding = QrVectorLogoPadding.Natural(.2f),
                    shape = QrVectorLogoShape.RoundCorners(.1f)
                )
            )
            .setBackground(
                QrVectorBackground(
                    color = QrVectorColor.Transparent
                )
            )
            .setColors(
                QrVectorColors(
                    dark = QrVectorColor.Solid(Color(0xff345288)),
                    ball = QrVectorColor.Solid(ContextCompat.getColor(this, R.color.black)),
                    frame =
//                    QrVectorColor.LinearGradient(
//                        colors = listOf(
//                            0f to Color.RED,
//                            1f to Color.BLUE
//                        ),
//                        orientation = QrVectorColor.LinearGradient.Orientation.LeftDiagonal
//                    )
                    QrVectorColor.Solid(ContextCompat.getColor(this, R.color.black))
                )
            )
            .setShapes(
                QrVectorShapes(
                    darkPixel = QrVectorPixelShape.RoundCorners(.15f),
                    ball = QrVectorBallShape.RoundCorners(0.25f),
                    frame = QrVectorFrameShape.RoundCorners(0.25f)
                )
            )
            .build()

        val qrCode: Drawable = QrCodeDrawable(data, options)
        binding.ivQrCode.setImageDrawable(qrCode)
    }
}