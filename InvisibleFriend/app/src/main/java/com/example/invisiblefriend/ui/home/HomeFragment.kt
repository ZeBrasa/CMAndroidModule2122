package com.example.invisiblefriend.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.invisiblefriend.MainActivity
import com.example.invisiblefriend.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //val textView: TextView = binding.textHome
        //homeViewModel.text.observe(viewLifecycleOwner, Observer {
        //   textView.text = it
        //})
        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }/*
    private fun checkWriteExternalPermission(): Boolean {
        val pm: PackageManager = context.getPackageManager()
        val hasPerm = pm.checkPermission(
            Manifest.permission.SEND_SMS,
            this.getBaseContext().getPackageName()
        )
        return if (hasPerm != PackageManager.PERMISSION_GRANTED) {
            false
        } else true
    }
    fun envairPapeis(tombola: ArrayList<Int>) {

        val telefones = ""
        val amigos = ""
        assert(telefones.length === amigos.length)
        for (i in 0 until amigos.length) {
            if (this.enviarPapelPorSms(
                    i,
                    tombola[i].intValue()
                )
            ) //this.papelEnviado2(i,tombola.get(i).intValue());
                this.papelEnviado(i) else this.erroAoEnviarPapel(i)
        }
    }

    fun enviarPapelPorSms(amigo: Int, papel: Int): Boolean {
        val msgPart1 =
            ""
        val msgPart2 = ".\nPara o ano ha mais.\nMesmo longe sempre no coração\nFELIZ NATAL"
        val mensagem = msgPart1 + amigos.get(papel).toString() + msgPart2
        try {
            val sms: SmsManager = SmsManager.getDefault()
            val parts: ArrayList<String> = sms.divideMessage(mensagem)
            sms.sendMultipartTextMessage(
                telefones2.get(amigo),
                null,
                parts,
                null,
                null
            ) //tests: envia tudo para o meu numero
            //sms.sendMultipartTextMessage(telefones[amigo], null, parts, null, null);
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return true
    }
    fun checkCorrespondenciasOK(tombola: ArrayList<Int>): Boolean {
        val tombola: ArrayList<Int> = MainActivity.range(0, MainActivity.amigos.length - 1)
        assert(tombola.size() === amigos.length)
        for (i in 0 until amigos.length) {
            if (i == tombola[i].intValue()) {
                return false
            }
        }
        return true
    }
    // Used to set the range of the list of friends in group
    fun range(min: Int, max: Int): ArrayList<Int>? {
        val list = ArrayList<Int>()
        for (i in min..max) {
            list.add(i)
        }
        return list
    }
*/
}