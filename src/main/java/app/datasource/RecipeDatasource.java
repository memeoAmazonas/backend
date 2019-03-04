package app.datasource;

import java.util.List;

import app.recipe.Ingredient;
import app.recipe.Recipe;


public interface RecipeDatasource {

	List<Recipe> findAll();
	boolean addRecipe(Recipe recipe);
	boolean updateRecipe(Recipe recipe);
	List<Ingredient> getIngredients();

}
