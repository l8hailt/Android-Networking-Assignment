package vn.hailt.hdwallpaper.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import vn.hailt.hdwallpaper.models.Media;
import vn.hailt.hdwallpaper.ultis.Config;
import vn.hailt.hdwallpaper.models.Category;
import vn.hailt.hdwallpaper.models.Post;

public interface WppService {

    @GET(Config.POST_URL)
    Call<List<Post>> getAllPosts();

    @GET(Config.POST_URL)
    Call<List<Post>> getPostsOfCategory(
            @Query("categories") int category_id
    );

    @GET(Config.CATEGORY_URL)
    Call<List<Category>> getAllCategories();

    @GET(Config.MEDIA_OF_POST_URL)
    Call<List<Media>> getMediaOfPost(
            @Query("parent") int parent);
}
