package com.dappslocker.bakingapp;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.dappslocker.bakingapp.repository.RecipesDataSource;
import com.dappslocker.bakingapp.repository.RecipesRepository;
import com.dappslocker.bakingapp.datasource.network.RemoteRecipesDataSource;
import com.dappslocker.bakingapp.model.Recipe;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

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
    RecipesDataSource recipesRemoteDataSource;

    @Mock
    RecipesDataSource localDataSource;

    @Mock
    private Context contextMock;

    @InjectMocks
    private RecipesRepository recipesRepository;


    @Before
    public void initialize(){
        MockitoAnnotations.initMocks(this);
        recipesRepository = null;
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
    public void whenGetRecipesIsCalledInitiallyThenA_NullListOfRecipiesIsReturned(){
        //given
        recipesRepository = RecipesRepository.getInstance(contextMock,recipesRemoteDataSource,localDataSource);
        //When
        LiveData<List<Recipe>> mCachedRecipes = recipesRepository.getRecipes();
        //Then
        assertThat(recipesRepository,is(not(nullValue())));
        assertEquals(null,mCachedRecipes);
    }

    @Test
    public void testt(){
        recipesRepository = RecipesRepository.getInstance(contextMock,recipesRemoteDataSource,localDataSource);
        recipesRepository.refreshRecipes();
        recipesRepository.getRecipes();
        verify(recipesRemoteDataSource, times(1)).getRecipes();
    }
    @Test
    public void whenGetRecipesIsCalledAndCacheIsDirtyThenA_RemoteDataSourceGetRecipiesIsInvoked(){
        doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                int i = 1;
                assertEquals(1,i);
                return null;
            }}).when(recipesRemoteDataSource).getRecipes();
        recipesRepository = RecipesRepository.getInstance(contextMock,recipesRemoteDataSource,localDataSource);
        recipesRepository.refreshRecipes();
        recipesRepository.getRecipes();
        verify(recipesRemoteDataSource, times(1)).getRecipes();
    }
}
