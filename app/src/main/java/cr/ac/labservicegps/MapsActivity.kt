package cr.ac.labservicegps

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import cr.ac.labservicegps.databinding.ActivityMapsBinding
import cr.ac.labservicegps.db.LocationDatabase
import cr.ac.labservicegps.entity.Location
import cr.ac.labservicegps.service.GpsService

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.PolyUtil
import com.google.maps.android.data.geojson.GeoJsonLayer
import com.google.maps.android.data.geojson.GeoJsonPolygon
import org.json.JSONObject

private lateinit var mMap: GoogleMap
private lateinit var locationDatabase: LocationDatabase
private lateinit var layer : GeoJsonLayer

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {


    private lateinit var binding: ActivityMapsBinding
    private val SOLICITAR_GPS = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        locationDatabase=LocationDatabase.getInstance(this)


        validaPermisos()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


        iniciaServicio()
        definePoligono(googleMap)
        recuperarPuntos(mMap)
    }
    /**
     * Obtener los puntos almacenados en la bd y mostrarlos en el mapa
     */
    fun recuperarPuntos(googleMap:GoogleMap){
        mMap = googleMap

        for(location in locationDatabase.locationDao.query()){
            val punto = LatLng(location.latitude, location.longitude)
            mMap.addMarker(MarkerOptions().position(punto).title("Marker in Costa Rica"))
        }

    }

    /**
     *hace un filtro del broadcast GPS(cr.ac.gpsservice.GPS_EVENT)
     * E inicia el servicio(starService) GpsService
     */

    fun iniciaServicio(){

        val filter= IntentFilter()
        filter.addAction(GpsService.GPS)
        val progressReceiver = ProgressReceiver()
        registerReceiver(progressReceiver,filter)
        startService(Intent(this,GpsService::class.java))
    }

    /**
     *Valida si la app tiene permisos de ACCESS_FINE_LOCATION y ACCESS_COARSE_LOCATION
     * si no tiene permisos solicita al usuario permisos(requestPermission)
     */
    fun validaPermisos(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            // NO TENGO PERMISOS
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), SOLICITAR_GPS)
        }
    }

    /**
     * validar que se le dieron los permisos a la app, en caso contrario salir
     */
    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            SOLICITAR_GPS -> {
                if ( grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    System.exit(1)
                }
            }
        }
    }
    fun definePoligono(googleMap: GoogleMap){
        val geoJsonData= JSONObject("{\n" +
                "  \"type\": \"FeatureCollection\",\n" +
                "  \"features\": [\n" +
                "    {\n" +
                "      \"type\": \"Feature\",\n" +
                "      \"properties\": {},\n" +
                "      \"geometry\": {\n" +
                "        \"type\": \"Polygon\",\n" +
                "        \"coordinates\": [\n" +
                "          [\n" +
                "            [\n" +
                "              -85.616455078125,\n" +
                "              11.210733765689508\n" +
                "            ],\n" +
                "            [\n" +
                "              -85.6494140625,\n" +
                "              11.102946786877578\n" +
                "            ],\n" +
                "            [\n" +
                "              -85.726318359375,\n" +
                "              10.962764256386821\n" +
                "            ],\n" +
                "            [\n" +
                "              -85.8251953125,\n" +
                "              10.898042159726009\n" +
                "            ],\n" +
                "            [\n" +
                "              -85.69335937499999,\n" +
                "              10.757762756247049\n" +
                "            ],\n" +
                "            [\n" +
                "              -85.726318359375,\n" +
                "              10.563422129963456\n" +
                "            ],\n" +
                "            [\n" +
                "              -85.8251953125,\n" +
                "              10.422988388338242\n" +
                "            ],\n" +
                "            [\n" +
                "              -85.869140625,\n" +
                "              10.282491301524178\n" +
                "            ],\n" +
                "            [\n" +
                "              -85.616455078125,\n" +
                "              9.871451997300548\n" +
                "            ],\n" +
                "            [\n" +
                "              -85.2978515625,\n" +
                "              9.871451997300548\n" +
                "            ],\n" +
                "            [\n" +
                "              -85.111083984375,\n" +
                "              9.654907854199024\n" +
                "            ],\n" +
                "            [\n" +
                "              -84.957275390625,\n" +
                "              9.774024565864734\n" +
                "            ],\n" +
                "            [\n" +
                "              -84.91333007812499,\n" +
                "              9.903921416774978\n" +
                "            ],\n" +
                "            [\n" +
                "              -85.166015625,\n" +
                "              9.958029972336439\n" +
                "            ],\n" +
                "            [\n" +
                "              -85.27587890625,\n" +
                "              10.196000424383561\n" +
                "            ],\n" +
                "            [\n" +
                "              -85.067138671875,\n" +
                "              10.14193168613103\n" +
                "            ],\n" +
                "            [\n" +
                "              -84.825439453125,\n" +
                "              10.012129557908155\n" +
                "            ],\n" +
                "            [\n" +
                "              -84.715576171875,\n" +
                "              9.795677582829743\n" +
                "            ],\n" +
                "            [\n" +
                "              -84.605712890625,\n" +
                "              9.524914302345891\n" +
                "            ],\n" +
                "            [\n" +
                "              -84.287109375,\n" +
                "              9.481572085088517\n" +
                "            ],\n" +
                "            [\n" +
                "              -83.70483398437499,\n" +
                "              9.037002898469423\n" +
                "            ],\n" +
                "            [\n" +
                "              -83.64990234375,\n" +
                "              8.874217009053567\n" +
                "            ],\n" +
                "            [\n" +
                "              -83.73779296875,\n" +
                "              8.678778692363062\n" +
                "            ],\n" +
                "            [\n" +
                "              -83.616943359375,\n" +
                "              8.461505694920898\n" +
                "            ],\n" +
                "            [\n" +
                "              -83.309326171875,\n" +
                "              8.407168163601076\n" +
                "            ],\n" +
                "            [\n" +
                "              -83.29833984375,\n" +
                "              8.51583556120223\n" +
                "            ],\n" +
                "            [\n" +
                "              -83.529052734375,\n" +
                "              8.678778692363062\n" +
                "            ],\n" +
                "            [\n" +
                "              -83.397216796875,\n" +
                "              8.700499129275828\n" +
                "            ],\n" +
                "            [\n" +
                "              -83.309326171875,\n" +
                "              8.624472107633936\n" +
                "            ],\n" +
                "            [\n" +
                "              -83.177490234375,\n" +
                "              8.548429781881927\n" +
                "            ],\n" +
                "            [\n" +
                "              -82.913818359375,\n" +
                "              8.102738577783168\n" +
                "            ],\n" +
                "            [\n" +
                "              -83.03466796874999,\n" +
                "              8.341953075466817\n" +
                "            ],\n" +
                "            [\n" +
                "              -82.825927734375,\n" +
                "              8.461505694920898\n" +
                "            ],\n" +
                "            [\n" +
                "              -82.869873046875,\n" +
                "              8.733077421211563\n" +
                "            ],\n" +
                "            [\n" +
                "              -82.73803710937499,\n" +
                "              8.90678000752024\n" +
                "            ],\n" +
                "            [\n" +
                "              -82.935791015625,\n" +
                "              9.047852691011155\n" +
                "            ],\n" +
                "            [\n" +
                "              -82.935791015625,\n" +
                "              9.427386615032363\n" +
                "            ],\n" +
                "            [\n" +
                "              -82.869873046875,\n" +
                "              9.557417356841308\n" +
                "            ],\n" +
                "            [\n" +
                "              -82.650146484375,\n" +
                "              9.514079262770904\n" +
                "            ],\n" +
                "            [\n" +
                "              -82.584228515625,\n" +
                "              9.524914302345891\n" +
                "            ],\n" +
                "            [\n" +
                "              -83.16650390625,\n" +
                "              10.087853819145947\n" +
                "            ],\n" +
                "            [\n" +
                "              -83.353271484375,\n" +
                "              10.304110328329449\n" +
                "            ],\n" +
                "            [\n" +
                "              -83.507080078125,\n" +
                "              10.444597722834875\n" +
                "            ],\n" +
                "            [\n" +
                "              -83.594970703125,\n" +
                "              10.617418067950293\n" +
                "            ],\n" +
                "            [\n" +
                "              -83.671875,\n" +
                "              10.951978221389624\n" +
                "            ],\n" +
                "            [\n" +
                "              -83.70483398437499,\n" +
                "              10.779348472547028\n" +
                "            ],\n" +
                "            [\n" +
                "              -83.968505859375,\n" +
                "              10.725381285457912\n" +
                "            ],\n" +
                "            [\n" +
                "              -84.144287109375,\n" +
                "              10.725381285457912\n" +
                "            ],\n" +
                "            [\n" +
                "              -84.254150390625,\n" +
                "              10.854886268472459\n" +
                "            ],\n" +
                "            [\n" +
                "              -84.375,\n" +
                "              10.951978221389624\n" +
                "            ],\n" +
                "            [\n" +
                "              -84.5068359375,\n" +
                "              11.016688524459864\n" +
                "            ],\n" +
                "            [\n" +
                "              -84.74853515625,\n" +
                "              11.059820828563412\n" +
                "            ],\n" +
                "            [\n" +
                "              -84.957275390625,\n" +
                "              10.984335146101955\n" +
                "            ],\n" +
                "            [\n" +
                "              -85.166015625,\n" +
                "              11.027472194117934\n" +
                "            ],\n" +
                "            [\n" +
                "              -85.40771484375,\n" +
                "              11.15684527521178\n" +
                "            ],\n" +
                "            [\n" +
                "              -85.616455078125,\n" +
                "              11.210733765689508\n" +
                "            ]\n" +
                "          ]\n" +
                "        ]\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}")

        layer = GeoJsonLayer(googleMap, geoJsonData)
        layer.addLayerToMap()

    }

    /**
     * es la clase para recibir los mensajes de broadcast
     */
    class ProgressReceiver:BroadcastReceiver(){

        fun getPolygon(layer: GeoJsonLayer): GeoJsonPolygon? {
            for (feature in layer.features) {
                return feature.geometry as GeoJsonPolygon
            }
            return null
        }

        /**
         * se obtiene el parametro enviado por el servicio (Location)
         * Coloca en el mapa la localizacion
         * Mueve la camara a esa localizacion
         */
        override fun onReceive(p0: Context, p1: Intent) {
            if(p1.action==GpsService.GPS){
                val ubicacion: Location = p1.getSerializableExtra("Localizacion") as Location
                val punto=LatLng(ubicacion.latitude,ubicacion.longitude)
                mMap.addMarker(MarkerOptions().position(punto).title("Marker in Costa Rica"))

                if(PolyUtil.containsLocation(ubicacion.latitude, ubicacion.longitude, getPolygon(layer)!!.outerBoundaryCoordinates, false)){
                    Toast.makeText(p0,"Si se encuentra en el punto ",Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(p0,"NO se encuentra en el punto ",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }




}