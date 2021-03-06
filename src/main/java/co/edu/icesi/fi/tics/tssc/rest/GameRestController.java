package co.edu.icesi.fi.tics.tssc.rest;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import co.edu.icesi.fi.tics.tssc.exceptions.CapacityException;
import co.edu.icesi.fi.tics.tssc.exceptions.GameException;
import co.edu.icesi.fi.tics.tssc.exceptions.SpringException;
import co.edu.icesi.fi.tics.tssc.model.TsscGame;
import co.edu.icesi.fi.tics.tssc.model.TsscStory;
import co.edu.icesi.fi.tics.tssc.services.GameService;
import co.edu.icesi.fi.tics.tssc.services.StoryService;

@RestController
public class GameRestController implements IGameRestController{

	private GameService gameService;
	private StoryService storyService;

	@Autowired
	public GameRestController(GameService gameService,StoryService storyService) {
		// TODO Auto-generated constructor stub
		this.gameService = gameService;
		this.storyService=storyService;
		
	}

	@PostMapping("/api/games/")
	public TsscGame saveGame(@RequestBody TsscGame tsscGame) {

		
		try {
			if (tsscGame.getTsscTopic() == null) {

				return gameService.saveGame(tsscGame);

			} else {

				return gameService.saveGameWithTopic(tsscGame, tsscGame.getTsscTopic().getId());
			}

		} catch (GameException | CapacityException | SpringException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	@PutMapping("/api/games-edit/")
	public TsscGame editGame(@RequestBody TsscGame editado) {

		try {
			return gameService.editGame(editado);
		} catch (GameException | CapacityException | SpringException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@GetMapping("/api/games/")
	public Iterable<TsscGame> findAll() {
		return gameService.findAll();

	}

	@GetMapping("/api/games/{id}")
	public TsscGame findById(@PathVariable("id") long id) {
		return gameService.findById(id).get();
	}

	@DeleteMapping("/api/games/{id}")
	public void deleteGame(@PathVariable("id") long id) {
		gameService.delete(gameService.findById(id).get());
	}
	
	
	@GetMapping("/api/games-date/{initial}/{final}")
	public Iterable<TsscGame> findByDate(@PathVariable("initial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate initialDate,@PathVariable("final") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  LocalDate finalDate) {
		return gameService.findByDate(initialDate, finalDate);

	}
	
	@GetMapping("/api/topics-date/{date}")
	public Iterable<Object[]> findTopicByGameDate(@PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		return gameService.findTopicByGameDate(date);

	}
	
	@PostMapping("/api/games/{id}/stories/add/")
	public TsscStory addStoryToGame(@PathVariable("id") long id, @RequestBody TsscStory tsscStory) {
		//TsscGame game =gameService.findById(id).get();
		System.out.println("ESTOY EN EL REST CONTROLLER" + tsscStory.getDescription());
	
		try {
			return storyService.saveStoryByGame(tsscStory, id);
			//return storyService.editStory(tsscStory);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;

	}
	
	@GetMapping("/api/games/{id}/stories")
	public Iterable<TsscStory> findAllStoriesByGame(@PathVariable("id") long id) {
		
		TsscGame game =gameService.findById(id).get();
		
		return game.getTsscStories();

	}


}
