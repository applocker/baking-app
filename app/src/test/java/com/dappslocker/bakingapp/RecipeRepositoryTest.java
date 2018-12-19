package com.dappslocker.bakingapp;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.dappslocker.bakingapp.datasource.network.RemoteRecipesDataSource;
import com.dappslocker.bakingapp.model.Recipe;
import com.dappslocker.bakingapp.repository.RecipesDataSource;
import com.dappslocker.bakingapp.repository.RecipesRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RecipeRepositoryTest {

    @Mock
    private
    RecipesDataSource recipesRemoteDataSource;

    @Mock
    private RecipesDataSource localDataSource;

    @Mock
    private Context contextMock;

    @InjectMocks
    private RecipesRepository recipesRepository;


    @Before
    public void initialize(){
        MockitoAnnotations.initMocks(this);
        recipesRepository = null;
    }

    @After
    public void reset(){
        recipesRemoteDataSource = null;
        localDataSource = null;
        contextMock = null;
    }
    @Test
    public void DummyTestToCheck_IsTestSetup_ReturnsTrue() {
        assertThat(RemoteRecipesDataSource.isTestSetup("test"), is(true));
    }

    @Test
    public void verifyRemoteRecipesDataSourceIsNullBeforeObtainingInstance(){
        assertEquals(null, recipesRepository);
    }

    @Test
    public void whenGetInstanceIsCalledThenWeGetAnInstanceOfTheRepository(){
        recipesRepository = RecipesRepository.getInstance(contextMock,recipesRemoteDataSource,localDataSource);
        assertThat(recipesRepository,is(not(nullValue())));
    }

    @Test
    public void whenGetRecipesIsCalledThenA_CacheIsReturned(){
        //given
        recipesRepository = RecipesRepository.getInstance(contextMock,recipesRemoteDataSource,localDataSource);
        //When
        LiveData<List<Recipe>> mCachedRecipes = recipesRepository.getRecipes();
        //Then
        assertThat(recipesRepository,is(not(nullValue())));
        assertThat(mCachedRecipes,is(not(nullValue())));
    }


    @Test
    public void whenGetRecipesIsCalledAndCacheIsDirtyThenA_RemoteDataSourceGetRecipiesIsInvoked(){
        final BooleanWrapper remoteDataSourceInvoked = new BooleanWrapper();
        remoteDataSourceInvoked.setInvoked(false);
        doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                remoteDataSourceInvoked.setInvoked(true);
                assertEquals(true, remoteDataSourceInvoked.isInvoked());
                return null;
            }}).when(recipesRemoteDataSource).getRecipes();
        recipesRepository = RecipesRepository.getInstance(contextMock,recipesRemoteDataSource,localDataSource);
        recipesRepository.refreshRecipes();
        recipesRepository.setRecipesRemoteDataSource(recipesRemoteDataSource);
        recipesRepository.getRecipes();
        verify(recipesRemoteDataSource, times(1)).getRecipes();

    }

    @Test
    public void whenGetRecipesIsCalledAndCacheIsNotDirtyThenA_LocalDataSourceGetRecipiesIsInvoked(){
        RecipesDataSource localDataSourceSpy = Mockito.spy(localDataSource);
        recipesRepository = RecipesRepository.getInstance(contextMock,recipesRemoteDataSource,localDataSource);
        recipesRepository.setRecipesRemoteDataSource(recipesRemoteDataSource);
        recipesRepository.setRecipesLocalDataSource(localDataSource);
        recipesRepository.getRecipes();
        verify(recipesRemoteDataSource, times(0)).getRecipes();
        verify(localDataSourceSpy, times(0)).getRecipes();
        assertEquals(false,recipesRepository.isCacheIsDirty());
    }

    private class BooleanWrapper{
        private boolean isInvoked;
        public boolean isInvoked() {
            return isInvoked;
        }
        public void setInvoked(boolean invoked) {
            isInvoked = invoked;
        }
    }
}
