package com.example.appdeprevisodotempo;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}

// ADICIONANDO DEPENDÊNCIAS

dependencies {
    implementation 'com.google.android.material:material:1.4.0'
    implementation 'androidx.recyclerview:recyclerview:1.2.1'
    implementation 'androidx.cardview:cardview:1.0.0'
    implementation 'com.google.code.gson:gson:2.8.7'
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    implementation 'com.google.android.gms:play-services-maps:17.0.0'
    implementation 'com.journeyapps:zxing-android-embedded:4.3.0'
}

// SPLASH SCREEN

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 3000); // 3 segundos de delay
    }
}

// ADAPTER PARA GERENCIAR FRAGMENTOS

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TabAdapter extends FragmentStateAdapter {
    public TabAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return position == 0 ? new ForecastFragment() : new MapFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

// ABA DE PREVISAO E MAPA

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ForecastFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forecast, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Configurar o adapter da RecyclerView
        recyclerView.setAdapter(new WeatherAdapter(/* dados da API */));
        return view;
    }
}

// RecyclerView para previsão do tempo

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    private final List<WeatherResponse.WeatherData.Forecast> forecastList;

    public WeatherAdapter(List<WeatherResponse.WeatherData.Forecast> forecastList) {
        this.forecastList = forecastList;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forecast, parent, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        WeatherResponse.WeatherData.Forecast forecast = forecastList.get(position);
        holder.dateTextView.setText(forecast.date);
        holder.weekdayTextView.setText(forecast.weekday);
        holder.tempTextView.setText(String.format("Máx: %d°C - Mín: %d°C", forecast.max, forecast.min));
    }

    @Override
    public int getItemCount() {
        return forecastList.size();
    }

    static class WeatherViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView;
        TextView weekdayTextView;
        TextView tempTextView;

        WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            weekdayTextView = itemView.findViewById(R.id.weekdayTextView);
            tempTextView = itemView.findViewById(R.id.tempTextView);
        }
    }
}

// MapFragment com GoogleMaps e marcador fixo

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng cityLocation = new LatLng(-23.5505, -46.6333); // Substitua pelas coordenadas da cidade
        googleMap.addMarker(new MarkerOptions().position(cityLocation).title("Cidade"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cityLocation, 10));
    }
}

// QR Code com ZXing para troca de cidade

FloatingActionButton qrButton = view.findViewById(R.id.qrButton);
qrButton.setOnClickListener(v -> {
IntentIntegrator integrator = new IntentIntegrator(getActivity());
    integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
    integrator.setPrompt("Escaneie o QR Code para alterar a cidade");
    integrator.setCameraId(0);
    integrator.setBeepEnabled(true);
    integrator.setBarcodeImageEnabled(true);
    integrator.initiateScan();
});





