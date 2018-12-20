package com.dappslocker.bakingapp;

import android.support.annotation.Nullable;

import com.dappslocker.bakingapp.datasource.network.GetRecipeDataService;
import com.dappslocker.bakingapp.datasource.network.RemoteRecipesDataSource;
import com.dappslocker.bakingapp.idlingResource.SimpleIdlingResource;
import com.dappslocker.bakingapp.model.Recipe;
import com.dappslocker.bakingapp.repository.DataSource.LoadRecipeCallback;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.dappslocker.bakingapp.utility.DataSourceUtils.DataSourceIdentifers;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@SuppressWarnings("unchecked")
public class RemoteRecipeDataSourceTest {
    @InjectMocks
    private RemoteRecipesDataSource remoteRecipesDataSource;


    @Test
    public void DummyTestToCheck_IsTestSetup_ReturnsTrue() {
        assertThat(RemoteRecipesDataSource.isTestSetup("test"), is(true));
    }


    @Test
    public void WhenLoadFromNetworkIsCalledThenOnRecipeLoadedCallbackIsCalledAfterASucessfullResponse(){
        LoadRecipeCallback callBack = mock(LoadRecipeCallback.class);
        GetRecipeDataService mockedService = Mockito.mock(GetRecipeDataService.class);
        final Call<ArrayList<Recipe>> mockedCall = Mockito.mock(Call.class);

        Mockito.when(mockedService.getRecipies()).thenReturn(mockedCall);

        Mockito.doAnswer(new Answer() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                Callback<ArrayList<Recipe>> callback = invocation.getArgument(0);
                callback.onResponse(mockedCall, Response.success(new ArrayList<Recipe>()));
                return null;
            }
        }).when(mockedCall).enqueue((Callback<ArrayList<Recipe>>) Mockito.<List<Recipe>>any());

        SimpleIdlingResource simpleIdlingResource = null;
        //noinspection ConstantConditions
        remoteRecipesDataSource = RemoteRecipesDataSource.getInstance(simpleIdlingResource);
        // inject mocks
        remoteRecipesDataSource.setLoadRecipeCallBack(callBack);
        remoteRecipesDataSource.setService(mockedService);
        //make a network call
        remoteRecipesDataSource.getRecipes();

        verify(mockedCall, times(1)).enqueue((Callback<ArrayList<Recipe>>) Mockito.<List<Recipe>>any());
        verify(callBack, times(1)).onRecipeLoaded( Mockito.<List<Recipe>>any(),
                Mockito.<DataSourceIdentifers>any());
    }
    @Test
    public void WhenLoadFromNetworkIsCalledThenonDataNotAvailableCallbackIsCalledForAnUnsucessfullResponse(){
        LoadRecipeCallback callBack = mock(LoadRecipeCallback.class);
        GetRecipeDataService mockedService = Mockito.mock(GetRecipeDataService.class);
        final Call<ArrayList<Recipe>> mockedCall = Mockito.mock(Call.class);

        Mockito.when(mockedService.getRecipies()).thenReturn(mockedCall);

        Mockito.doAnswer(new Answer() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                Callback<ArrayList<Recipe>> callback = invocation.getArgument(0);
                callback.onResponse(mockedCall, Response.<ArrayList<Recipe>>error(404, new ResponseBody() {
                    @Nullable
                    @Override
                    public MediaType contentType() {
                        return null;
                    }

                    @Override
                    public long contentLength() {
                        return 0;
                    }

                    @Override
                    public BufferedSource source() {
                        return null;
                    }
                }));
                return null;
            }
        }).when(mockedCall).enqueue((Callback<ArrayList<Recipe>>) Mockito.<List<Recipe>>any());

        SimpleIdlingResource simpleIdlingResource = null;
        //noinspection ConstantConditions
        remoteRecipesDataSource = RemoteRecipesDataSource.getInstance(simpleIdlingResource);
        // inject mocks
        remoteRecipesDataSource.setLoadRecipeCallBack(callBack);
        remoteRecipesDataSource.setService(mockedService);
        //make a network call
        remoteRecipesDataSource.getRecipes();

        verify(mockedCall, times(1)).enqueue((Callback<ArrayList<Recipe>>) Mockito.<List<Recipe>>any());
        verify(callBack, times(1)).onDataNotAvailable(Mockito.<DataSourceIdentifers>any());
    }


}
