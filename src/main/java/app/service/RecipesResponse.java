package app.service;

import java.util.ArrayList;
import java.util.List;

import app.recipe.Recipe;


public class RecipesResponse extends AbstractResponse {

	private List<Recipe> recipes;

	public List<Recipe> getRecipes() {
		if(recipes==null)
			recipes= new ArrayList<Recipe>();
		return recipes;
	}

	public void setRecipes(List<Recipe> recipes) {
		this.recipes = recipes;
	}

}
