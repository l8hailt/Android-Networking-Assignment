package vn.hailt.hdwallpaper.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vn.hailt.hdwallpaper.ultis.Config;

public class WppRetrofit {

    private static WppService instance;

    public static WppService getInstance() {

        if (instance == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Config.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            instance = retrofit.create(WppService.class);
        }
        return instance;
    }
}
