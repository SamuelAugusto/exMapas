package com.example.exmapas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Placeholder;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private static boolean LOCATION_PERMISSION_REQUEST_CODE = false;
    public GoogleMap mapa;
    public LatLng localizacao = new LatLng(-23.951137, -46.339025);
    //coordenadas iniciais
    private Button BtMinhaPosicao;
    private GeoDataClient geoDataClient;
    //Objeto que permite a exibiçã0 dos lugares proximo as coordenadas
    private FusedLocationProviderClient mFusedLocationProviderClient;
    //Objeto que prove a geulocalizacao

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().
                findFragmentById(R.id.nossoMapa);
        //Referencia ao fragment do mapa
        mapFragment.getMapAsync(MainActivity.this);
        //Sincronização do mapa nesta activity
        geoDataClient = Places.getGeoDataClient(MainActivity.this, null);
        //Exibe os lugares proximos
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        //Busca o client para pegar geolocalização
        metodoBotao();
    }

    public void metodoBotao(){
        BtMinhaPosicao=(Button)findViewById(R.id.btnMinhaPosicao);
        BtMinhaPosicao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableMyLocation();
                atualizaSuaLocalizacao();
            }
        });
    }

    private void enableMyLocation(){
        if((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED));
        {
            LOCATION_PERMISSION_REQUEST_CODE= PermissionUtils.validate(this, 1, Manifest.permission.ACCESS_FINE_LOCATION);
            LOCATION_PERMISSION_REQUEST_CODE= PermissionUtils.validate(this, 1, Manifest.permission.ACCESS_COARSE_LOCATION);
            atualizaSuaLocalizacao();
        }


    }

    private void atualizaSuaLocalizacao(){
        try {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mapa.addMarker(new MarkerOptions().position(latLng).title("Diga oi !!"));

                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng,18);

                    mapa.animateCamera(update);
                    mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mapa = googleMap;
        //atribui o objeto mapa recebido
        mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        //seta o tipo de visualização
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(localizacao,18);
        //adiciona um efeito para fazer a aproximação da camera numa localização com altura 18
        mapa.animateCamera(update);
        //aplica o comando acima

        //Adicionado um circulo em volta de uma geolocalização
        Circle circle = mapa.addCircle(new CircleOptions()
                .center(localizacao)
                .radius(100)
                .strokeColor(Color.RED)
                .fillColor(Color.TRANSPARENT));

         mapa.addMarker(new MarkerOptions().position(localizacao).title("SFC"));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


}
