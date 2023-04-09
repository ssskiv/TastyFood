package com.lyeleven.tastyfood

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.InputType
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.nio.channels.Channels
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private var stringList: MutableList<MutableList<String>> = mutableListOf(mutableListOf(""))
    private val string: List<String> = listOf(
        "1&11290350&На кухне&21.12490599677003&34.86727674147133&81.53111262035883&70.61448415902554&6\n",
        "2&4622733&Обработка&95.32276068333488&41.36679667033229&53.83410922788674&38.87781206937145&11\n",
        "3&5234323&На кухне&12.274724556543081&2.0229787068000493&50.1374812432241&59.340993805150724&12\n",
        "4&9069148&В пути&92.43343302897597&37.60531502713632&26.084246974301408&73.80338394889927&3\n",
        "5&8549345&Завершен&45.013933364948585&93.57931325270992&81.97542351732238&17.535726837374366&10\n",
        "6&5034970&Обработка&50.38094599226204&29.772363300134007&82.55882441150929&89.96738915616027&5\n",
        "7&1452597&На кухне&83.67744027258318&18.14428587290571&12.520835709314815&74.26567518017166&13\n",
        "8&958003&На кухне&43.79043882122834&58.314463808173&23.4687227444086&93.24172400085845&1\n",
        "9&3697717&Завершен&77.31120767803553&28.592831707512822&88.56351900200774&93.26037859882771&2\n",
        "10&9909869&Завершен&14.52904153208766&94.44745638358789&70.90597329783087&98.33029974216223&18\n",
        "11&4573443&Завершен&55.50513259922902&87.50534436750873&38.679783240385056&11.702341844742259&20\n",
        "12&2650238&На кухне&52.73618142153105&87.4493296368501&82.06268591633064&81.68795705201369&1\n",
        "13&10089256&На кухне&60.56448201152218&52.150218241598665&29.756016435098875&35.52373390085483&3\n",
        "14&5518035&Обработка&0.48620357952661175&29.8144887943636&77.95125256331704&60.34299456709664&14\n",
        "15&11719009&Завершен&65.66925150740344&47.83585194832308&78.22551358082384&69.0467788459383&18\n",
        "16&7126455&Завершен&54.99545293394471&41.449485849903944&43.159457498868356&80.85709317297233&1\n",
        "17&6027297&Завершен&76.65545686964828&27.4927032703867&46.06335631662857&71.58386278980403&19\n",
        "18&8384252&Обработка&70.93234874095523&79.36186694371848&13.925576262547478&51.71324875893962&10\n",
        "19&5475356&Обработка&15.68722908309037&20.74491727021458&61.05596058685231&71.73354757436188&5\n",
        "20&753314&На кухне&92.24002261525317&6.905636193618758&48.42321260125745&26.179148934833087&8\n",
        "21&8575854&В пути&5.626617953438117&15.46335278697396&46.16388487364493&81.61826485708757&1\n",
        "22&9313065&В пути&61.47912535693699&58.84440648824376&34.19551230724155&23.398697222438834&11\n",
        "23&7504503&В пути&21.112736429200453&83.45286567184044&66.53286272241931&90.44917478323846&14\n",
        "24&4018618&В пути&13.554815223030802&19.786381578751268&60.046708614176026&11.793977761039265&20\n",
        "25&2925398&На кухне&52.828265280231534&42.55845893551342&64.31124510484561&63.45265678627875&9\n",
        "26&2577317&Завершен&58.730219956278475&34.78995476980556&43.97260462968913&14.371391600701845&13\n",
        "27&12104465&Завершен&7.88960383039885&59.529194683300744&65.58068072098034&55.3018504529548&10\n",
        "28&9395413&На кухне&52.303707045374004&60.301566695575794&95.87061846732283&6.423394825229245&1\n",
        "29&360883&Завершен&41.920842483198776&92.72239008404448&72.23466065763407&48.18567880092532&9\n",
        "30&11675401&Обработка&34.765093188376184&36.5294304063921&98.37948145537608&4.568857716845908&16\n",
        "31&9753213&На кухне&8.63508255543538&75.04157824815655&24.638180151048694&99.37904175476648&17\n",
        "32&11618382&Обработка&18.584840382009247&83.83927206692746&97.82439047451786&47.77332723078159&5\n",
        "33&9851148&На кухне&76.30194223876778&12.725527974635419&67.81031174577392&87.99552197235566&20\n",
        "34&5397258&Обработка&97.83500192035781&46.59129893942875&26.517046296480196&9.902715027329611&3\n",
        "35&7784175&В пути&2.9844461503579445&90.49603005760758&15.861357962311551&20.001350665901917&8\n",
        "36&3507466&Завершен&31.656927665092926&10.604606275422112&97.47120493573256&21.55548908945898&10\n",
        "37&10507476&Завершен&33.53833032820207&30.25628792919166&12.221594066020902&86.98507620526523&15\n",
        "38&12298983&Обработка&69.8379879677628&34.681334881738934&12.130019805471237&3.2442288625729065&11\n",
        "39&9103869&На кухне&91.61404977406397&81.72599922281583&54.60456184691258&81.80602216333975&7\n",
        "40&5614618&Завершен&14.583667545013768&0.9562289789686429&53.182322822973774&28.508144164846495&14\n",
        "41&1385065&В пути&46.64543431828512&91.96168920158499&86.55667617101884&33.66157866623216&8\n",
        "42&6695170&В пути&50.20701040210501&96.96648655754872&19.17734370156249&81.9219183705371&12\n",
        "43&1090046&Завершен&83.64620508106246&24.35304603358973&53.79955214283735&39.39111727908789&12\n",
        "44&6895717&Обработка&90.95672310231157&71.58991780383303&38.09378066405782&9.065696651256506&1\n",
        "45&2340461&В пути&53.196267429591096&3.2249272146368413&98.2645093503692&30.178870853180207&5\n",
        "46&8119411&В пути&6.993961550889294&34.4133373553836&16.824851786020457&5.6632528696978435&1\n",
        "47&238182&В пути&81.30019284144956&94.99660813353773&45.940891482002485&5.262765122058832&14\n",
        "48&3842400&На кухне&41.339056481052125&76.64848755988768&14.496721638622457&24.897462527557035&11\n",
        "49&5202126&В пути&69.52985705801701&22.52495489204587&58.35613385505618&43.955758350863306&14\n",
        "50&9957828&Обработка&42.002569038824035&76.14528985005295&16.684543826200493&8.398550550876783&15\n",
        "51&72270&Обработка&34.154956297325946&62.01287125513068&59.77919674721031&21.049771052370215&4\n",
        "52&6352162&На кухне&99.6112047854619&14.480713997158912&22.952167206289474&84.05810901520367&19\n",
        "53&4553756&На кухне&37.891043083150436&74.71459075387166&95.45461579485035&99.29035158474775&12\n",
        "54&11639226&Обработка&45.120555925547855&70.79126648060124&40.2139540628711&50.969469513837176&18\n",
        "55&2606066&Завершен&98.70334180045488&41.78456962609574&65.46647940574849&4.200681776356607&1\n",
        "56&7192150&На кухне&75.66725988255764&93.79864809527373&8.472762164190561&95.57331150811986&9\n",
        "57&4525121&На кухне&25.47488884852134&44.669282409540365&99.22612989460245&93.93232311934963&14\n",
        "58&3035881&Завершен&93.77860153325052&52.78363988186867&96.14074163896605&94.16523947631347&17\n",
        "59&2070040&Завершен&78.33493620306874&2.906743718029059&21.856589114675806&40.753772780109884&9\n",
        "60&60312&Обработка&70.88665288692808&84.21326228753239&28.115199844730064&62.412724481235834&4\n",
        "61&7410398&В пути&73.57978380651315&92.46976856467057&91.4834469740715&55.97518784740113&15\n",
        "62&517559&На кухне&38.900917240709454&93.98744162819112&11.90284646548495&13.745758180051748&10\n",
        "63&12257303&Обработка&48.434896354736765&83.66120338589911&70.4835636255314&54.072960986566834&8\n",
        "64&9621259&На кухне&12.17178516808377&41.37967087397546&32.90110554343701&30.978357524543643&10\n",
        "65&7189795&Обработка&15.730209434514508&0.3522363742557433&56.76531425326434&21.567493031857452&13\n",
        "66&8848646&Завершен&97.0842616751533&62.31267295580245&48.69607324173521&70.24020644057705&8\n",
        "67&9151163&Завершен&13.933279663279974&82.54666078598018&45.07314113982005&32.328452750132776&20\n",
        "68&6054168&На кухне&87.88776130928305&51.38725762859491&33.35002167025818&70.12422218402627&5\n",
        "69&6630181&В пути&19.40426774402996&94.95403834882333&82.41911448523858&88.80024395857117&14\n",
        "70&1079274&Обработка&30.762126242287625&80.94842576508384&15.390430749649553&26.1455308668198&14\n",
        "71&7174147&Обработка&30.908503855288117&85.00391563112208&35.82566726171811&39.96906244495406&20\n",
        "72&5273570&Завершен&20.838812568298415&93.36626284658182&56.07658506618461&11.462228160928856&6\n",
        "73&7786110&На кухне&36.046943939227724&99.8329238784546&15.691990567461044&95.40871287870296&6\n",
        "74&897555&На кухне&37.01320728649786&94.72907873528598&12.893107980329987&30.44509015511766&8\n",
        "75&2136247&На кухне&61.352726431188174&18.290048929321735&82.61591592019201&66.63042730179889&7\n",
        "76&5248684&Завершен&77.76387389354123&30.281349518686785&80.35579066013257&48.7574415665972&4\n",
        "77&4794183&На кухне&25.012230634956467&41.30135519399677&83.34538780410986&73.54623690916004&11\n",
        "78&4127480&Обработка&60.895775588229405&35.91951193583726&79.78515103423409&70.65239356845608&12\n",
        "79&5219600&На кухне&90.09790260789498&14.637256921850062&4.190304980346737&39.16598516647631&19\n",
        "80&9044857&В пути&26.47455946667496&88.79799573739741&38.97675795809681&27.65699586881182&4\n",
        "81&10985885&На кухне&3.9827642035679456&43.870845302147565&63.28633156725434&97.13912801469115&5\n",
        "82&12307844&Завершен&71.93477986864278&42.75366221296547&18.966524054906387&33.98161639531862&16\n",
        "83&11323166&В пути&10.268992418857026&79.33322333601438&42.80073653962039&78.52821074380408&8\n",
        "84&11660507&Обработка&41.7129598962098&8.151173677602841&81.90048132966719&58.28777479310464&18\n",
        "85&9546044&Завершен&32.68526599169333&98.15960152890484&19.718490529726584&42.64526660173762&5\n",
        "86&11964739&Обработка&6.601434067198364&10.280662291983745&24.640751562965267&32.46713202272705&6\n",
        "87&3088407&На кухне&84.15316048142734&72.22153896688943&78.50899978175859&53.36540520098657&9\n",
        "88&1158873&На кухне&61.41339827620956&10.517398961732061&7.8031761097294705&90.45846662247321&19\n",
        "89&8072292&Обработка&31.435444911878818&48.74472326860756&79.94517743603835&31.14341262703585&14\n",
        "90&83738&В пути&40.6986481382149&74.26805583593132&50.88801332107229&28.07951981551803&9\n",
        "91&166596&Обработка&99.29146819846302&89.1681735534448&29.750667499937755&58.88049300032612&17\n",
        "92&9839276&Обработка&68.27279354818832&19.692020621348018&94.42985894319388&11.37444345203502&16\n",
        "93&8107978&В пути&31.96468823097234&73.92256478068934&42.65529745508931&56.301215738184176&14\n",
        "94&6554664&Завершен&87.90587980900983&33.83782907328493&89.72234951756508&57.52000445114187&19\n",
        "95&2813861&Завершен&34.82330172129129&40.46207340335128&77.81318378464232&76.05051234718442&4\n",
        "96&11946841&Завершен&73.09281822448906&10.009866940976163&95.52656830960144&67.86897662992668&9\n",
        "97&1563578&Обработка&41.13329931766092&47.37437772729224&50.45218879164104&10.980544405721758&15\n",
        "98&9965757&На кухне&51.101538393770966&44.30122096612274&88.93635547922437&50.80143117882927&1\n",
        "99&7869907&Обработка&3.3123332532850824&67.54530586692216&38.07215279542794&35.133921672633626&11"
    )
    private var courierNumber: Int = 0
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var isGPSActivated = false

    private val defaultURL = URL("https://127.0.0.1:5501/")

    @SuppressLint("UseSwitchCompatOrMaterialCode", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var dalt = 47.2251069
        var dlong = 39.7746818

        stringList.removeAt(0)
        for (i in string.indices) {
            stringList.add(string[i].split('&').toMutableList())
        }

        val buttonNavigate = findViewById<Button>(R.id.navigate_button)
        val tv = findViewById<TextView>(R.id.tv)
        val radioButton = findViewById<Switch>(R.id.rb)
        val reloadButton = findViewById<ImageButton>(R.id.reload_button)
        val phoneNumber = findViewById<TextView>(R.id.textView3)
        val status = findViewById<TextView>(R.id.textView)
        val time = findViewById<TextView>(R.id.time)
        //Log.d("NET", defaultURL.toString())
        val permissionManager = PermissionManager()
        permissionManager.showLocationPermissions(this)
        buttonNavigate.isEnabled = radioButton.isChecked
        reloadButton.isEnabled = radioButton.isChecked
        radioButton.setOnClickListener {
            isGPSActivated =
                radioButton.isChecked/*buttonNavigate.isEnabled = radioButton.isChecked*/
            buttonNavigate.isEnabled = radioButton.isChecked
            reloadButton.isEnabled = radioButton.isChecked
        }
        buttonNavigate.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,/*saddr=47.224645,39.776141&*/
                Uri.parse("http://maps.google.com/maps?daddr=$dalt,$dlong")
            )
            startActivity(intent)
        }

        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        if (sharedPref.getString(getString(R.string.courier_num_key), "0") == "0") {
            enterCourierNumber()
        }
        var a: MutableList<MutableList<String>> = mutableListOf(mutableListOf())
        reloadButton.setOnClickListener {
            a = mutableListOf(mutableListOf())
            a.removeAt(0)
            for (i in stringList.indices) {
                if (stringList[i][2] == "В пути" && stringList[i][7] == sharedPref.getString(
                        getString(R.string.courier_num_key),
                        "0"
                    ) + "\n"
                ) {
                    a.add(stringList[i].toMutableList())
                }
            }
            tv.text = "ЗАКАЗ №${a[0][0]}ПРИНЯТ"
            dalt = a[0][3].toDouble()
            dlong = a[0][4].toDouble()
            status.text = a[0][2]
            time.text = a[0][1]
        }

    }

    /* private fun downloadFile(url: URL, outputFileName: String) {
         Log.d("THREAD", "Starting thread.. Trying to connect $url")
         val request=DownloadManager.Request(Uri.parse(url.toString()))
             .setTitle("variables.txt")
             .setMimeType("text/txt")
             .setDescription("Downloading...")
             .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
             .setAllowedOverMetered(true)
             .setDestinationInExternalFilesDir(this,Environment.DIRECTORY_DOCUMENTS,outputFileName)
             .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
         val dm= getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
         Log.d("THREAD", "Trying to download.. Trying to connect $url")
         dm.enqueue(request)
     }
 */
    private fun filterFile(file: File, courierNumber: Int): Order {
        var order: Order = Order(0, 0.toString(), "0", 0f, 0f, 0f, 0f)
        if (file.exists())
            file.forEachLine {
                val list = it.split("&")
                val orderNumber = list[0].toInt()
                val time = list[1]
                val orderStatus = list[2].toString()
                val calt = list[3].toFloat()
                val clong = list[4].toFloat()
                val dalt = list[5].toFloat()
                val dlong = list[6].toFloat()
                val _courierNumber = list[7].toInt()
                if (_courierNumber == courierNumber && orderStatus == "В пути") {
                    order = Order(orderNumber, time, orderStatus, calt, clong, dalt, dlong)
                    return@forEachLine
                }
            }
        else
            Log.e("FUCK", "FUCKFUCKFUCK")
        return order
    }


    private fun filterUrl(url: URL, courierNumber: Int): Order {
        //var nurl = URL(url.toString() , "variables.txt")
        //downloadFile(nurl, "variables.txt")
        return  /*Order(0, 0.toString(), "0", 0f, 0f, 0f, 0f)*/filterFile(
            File("variables.txt"),
            courierNumber
        )
    }


    private fun enterCourierNumber(): Int {
        val alert = AlertDialog.Builder(this)
        alert.setTitle("Введите номер курьера")
        val editText = EditText(this)
        editText.inputType = InputType.TYPE_CLASS_TEXT
        alert.setView(editText)
        var a = "0"
        alert.setPositiveButton("Ok") { _, _ ->
            a = editText.text.toString()
            val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString(getString(com.lyeleven.tastyfood.R.string.courier_num_key), a)
                apply()
            }
        }
        alert.show()
        return a.toInt()
    }
}