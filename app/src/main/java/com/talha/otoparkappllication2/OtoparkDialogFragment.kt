import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.talha.otoparkappllication2.DatabaseHelper
import com.talha.otoparkappllication2.OtoparkModel
import com.talha.otoparkappllication2.R
import com.talha.otoparkappllication2.databinding.FragmentOtoparkDialogBinding

class OtoparkDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentOtoparkDialogBinding
    private lateinit var otopark: OtoparkModel
    private lateinit var helper: DatabaseHelper

    companion object {
        fun newInstance(otopark: OtoparkModel): OtoparkDialogFragment {
            val fragment = OtoparkDialogFragment()
            fragment.otopark = otopark
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOtoparkDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Otopark bilgilerini dialog içindeki alanlara yerleştirme işlemleri burada yapılır.
        binding.etOtoparkAdi.setText(otopark.otoparkAdi)
        binding.etOtoparkAdresi.setText(otopark.otoparkAdresi)
        binding.etOtoparkKapasitesi.setText(otopark.otoparkKapasitesi.toString())

        // DatabaseHelper nesnesini oluştur
        helper = DatabaseHelper(requireContext())
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Update Info")

        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_otopark_dialog, null)
        val OtoparkAdi: EditText = dialogView.findViewById(R.id.etOtoparkAdi)
        val Otoparkaddresi: EditText = dialogView.findViewById(R.id.etOtoparkAdresi)
        val Otoparkkapasitesi: EditText = dialogView.findViewById(R.id.etOtoparkKapasitesi)

        // Otopark objesinden değerleri EditText'lere yerleştir
        OtoparkAdi.setText(otopark.otoparkAdi)
        Otoparkaddresi.setText(otopark.otoparkAdresi)
        Otoparkkapasitesi.setText(otopark.otoparkKapasitesi.toString())

        builder.setView(dialogView)

        builder.setPositiveButton("Update", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                // Güncelleme işlemleri burada yapılır.
                val isUpdate = helper.otoparkGuncelle(
                    otopark.otoparkId,
                    OtoparkAdi.text.toString().trim(),
                    Otoparkaddresi.text.toString().trim(),
                    Otoparkkapasitesi.text.toString().toInt()
                )
                if (isUpdate == 1) {
                    if (isAdded) {
                        Toast.makeText(requireContext(), "Updated :)", Toast.LENGTH_LONG).show()
                    }

                    // İşlem tamamlandıktan sonra YetkiliArayuzActivty ekranına geri dön
                    requireActivity().finish()
                } else {
                    if (isAdded) {
                        Toast.makeText(requireContext(), "failed update :(", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })

        builder.setNegativeButton("Sil", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                // Silme işlemleri burada yapılır.
                deleteOtoparkFromDatabase(otopark.otoparkId)
            }
        })

        builder.setNeutralButton("İptal", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                // İptal işlemleri burada yapılır.
            }
        })

        return builder.create()
    }

    private fun deleteOtoparkFromDatabase(otoparkId: Long) {
        val rowsAffected = helper.otoparkSil(otoparkId)

        if (rowsAffected > 0) {
            // Silme başarılı
            showToast("Silme başarılı")

            // İşlem tamamlandıktan sonra YetkiliArayuzActivty ekranına geri dön
            requireActivity().finish()
        } else {
            // Silme başarısız
            showToast("Silme başarısız")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
