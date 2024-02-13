package com.example.cardapio.controller;

import com.example.cardapio.food.Food;
import com.example.cardapio.food.FoodRepository;
import com.example.cardapio.food.FoodRequestDTO;
import com.example.cardapio.food.FoodResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("food")
public class FoodController {

    @Autowired
    private FoodRepository repository;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping
    public void saveFood(@RequestBody FoodRequestDTO data){
        Food foodData = new Food(data);
        repository.save(foodData);
        return;
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping
    public List<FoodResponseDTO> getAll(){

        List<FoodResponseDTO> foodList = repository.findAll().stream().map(FoodResponseDTO::new).toList();
        return foodList;
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateFood(@PathVariable Long id, @RequestBody FoodRequestDTO data) {
        Optional<Food> optionalFood = repository.findById(id);
        if (optionalFood.isPresent()) {
            Food food = optionalFood.get();
            food.setTitle(data.title());
            food.setImage(data.image());
            food.setPrice(data.price());
            repository.save(food);
            return ResponseEntity.ok().build();
        } else {
            throw new EntityNotFoundException("Food not found with id: " + id);
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deleteFood(@PathVariable Long id){
        Optional<Food> optionalFood = repository.findById(id);
        if(optionalFood.isPresent()){
            Food food = optionalFood.get();
            repository.delete(food);
            return ResponseEntity.noContent().build();
        } else {
            throw new EntityNotFoundException("Food not found with id: " + id);
        }
    }

}
