package com.senjapagi.kolaborasi.Services

object URL {
        const val website = "https://sas.badanmentoring.org"
        const val base_url = "http://103.253.27.125:10000/Kolaborasi/"
        const val BASE_URL = "http://103.253.27.125:10000/Kolaborasi/"
        const val PROFILE_PIC_URL = "http://103.253.27.125:10000/Kolaborasi/ProfilePic/"
        const val GET_DETAIL_USER = "${BASE_URL}getDetailPeserta.php"
        const val UPDATE_PESERTA = "${BASE_URL}updatePeserta.php"
        const val UPDATE_PESERTA_PROFILE = BASE_URL + "updateFotoProfile.php"
        const val LOGIN_ALL = BASE_URL + "loginPeserta.php"


        const val REGISTASI_PESERTA = BASE_URL + "registrasiPeserta.php"




        const val LOGIN_ADMIN = base_url + "login_admin.php"


        const val RESET_PASSWORD = base_url + "forgotPassword.php"

        const val RETREIVE_PESERTA = base_url + "getPeserta.php"


        const val login_api = base_url + "login_mentee.php"
        const val small_class_score = base_url + "getNilai.php"
        const val lihat_kelompok = base_url + "show_data.php"
        const val updateBasicMentee = base_url + "updateMenteeMhs.php"
        const val updateBasicMentor = base_url + "updateMentorMhs.php"
        const val getBasicMentee = base_url + "getDataMentee.php"
        const val getBasicMentor = base_url + "getDataMentor.php"
        const val getKelompok = base_url + "getKelompok.php"
        const val updatePasswordMentee = base_url + "updatePasswordMentee.php"
        const val updatePasswordMentor = base_url + "updatePasswordMentor.php"
        const val updateHelpdesk = base_url + "updateKontak.php"
        const val uploadTubes = base_url + "insert_tubes.php"
        const val getKontak = base_url + "getHelpdesk.php"
        const val getAgenda = base_url + "getAgenda.php"
        const val getAgendaGeneral = base_url + "getAgendaGeneral.php"
        const val getAgendaTalaqqi = base_url + "getAgendaTalaqqi.php"
        const val insertPresensiGeneral = base_url + "insert_presensi_general.php"
        const val insertPresensiTalaqqi = base_url + "insert_presensi_talaqqi.php"
        const val insertKontak = base_url + "insert_kontak.php"
        const val loginMentor = base_url + "login_mentor.php"


}