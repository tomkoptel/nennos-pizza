package com.sample.nennos.persistence

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

object Seed : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        db.apply {
            execSQL("INSERT into Pizza (uid, name, imageUrl, basePrice) VALUES ('2bb06763-4519-4404-b074-831901d8f84a', 'Hawai', 'https://dtgxwmigmg3gc.cloudfront.net/files/53a93e0fe1272f237400ab93-thumb-256x256.jpeg', 10.0);")
            execSQL("INSERT into Pizza (uid, name, imageUrl, basePrice) VALUES ('a6041e38-3a4b-4573-8e57-28d1ee180310', 'Chili', 'https://bigoven-res.cloudinary.com/image/upload/t_recipe-256/pita-pizza-vegan.jpg', 9.0);")
            execSQL("INSERT into Ingredient (uid, name, price) VALUES ('c24d982f-83e0-4b5f-94ff-4272366cf410', 'Cheese', 5.0);")
            execSQL("INSERT into Ingredient (uid, name, price) VALUES ('32bcad2a-9179-4b5a-9f07-0d6c9f950f40', 'Ananas', 10.0);")
            execSQL("INSERT into Ingredient (uid, name, price) VALUES ('f0118d3c-10de-45e5-8a8e-7dd1dbc6c333', 'Chili', 9.0);")
            execSQL("INSERT into Ingredient (uid, name, price) VALUES ('077b2d38-ada3-4c28-82e4-48fe8755bc37', 'Tofu', 1.0);")
            execSQL("INSERT into Ingredient (uid, name, price) VALUES ('918f1140-2e57-4ed9-9a7b-030199f714c9', 'Onion', 1.0);")
            execSQL("INSERT into Ingredient (uid, name, price) VALUES ('6a31d0e0-9565-49ce-8684-0370ec587969', 'Olive', 1.0);")
            execSQL("INSERT into Ingredient (uid, name, price) VALUES ('6ad123c3-b7f6-43a2-8b66-3362382638b7', 'Bacon', 1.0);")
            execSQL("INSERT into Ingredient (uid, name, price) VALUES ('29cb77d1-a837-4a61-aeb5-b79483716815', 'Quark', 1.0);")
            execSQL("INSERT into Ingredient (uid, name, price) VALUES ('b843528b-d87f-4050-ae94-c0f532ca76b3', 'Tomato', 1.0);")
            execSQL("INSERT into Ingredient (uid, name, price) VALUES ('54f12465-5df5-4c87-9de6-32a17de7c47f', 'Paprika', 1.0);")
            execSQL("INSERT into Ingredient (uid, name, price) VALUES ('8147c56f-0072-4b46-8aaf-b762699c5ffb', 'Garlic', 1.0);")
            execSQL("INSERT into PizzaIngredient (pizzaId, ingredientId) VALUES ('2bb06763-4519-4404-b074-831901d8f84a', 'c24d982f-83e0-4b5f-94ff-4272366cf410');")
            execSQL("INSERT into PizzaIngredient (pizzaId, ingredientId) VALUES ('2bb06763-4519-4404-b074-831901d8f84a', '32bcad2a-9179-4b5a-9f07-0d6c9f950f40');")
            execSQL("INSERT into PizzaIngredient (pizzaId, ingredientId) VALUES ('a6041e38-3a4b-4573-8e57-28d1ee180310', 'c24d982f-83e0-4b5f-94ff-4272366cf410');")
            execSQL("INSERT into PizzaIngredient (pizzaId, ingredientId) VALUES ('a6041e38-3a4b-4573-8e57-28d1ee180310', 'f0118d3c-10de-45e5-8a8e-7dd1dbc6c333');")
            execSQL("INSERT into PizzaIngredient (pizzaId, ingredientId) VALUES ('a6041e38-3a4b-4573-8e57-28d1ee180310', '077b2d38-ada3-4c28-82e4-48fe8755bc37');")
            execSQL("INSERT into PizzaIngredient (pizzaId, ingredientId) VALUES ('a6041e38-3a4b-4573-8e57-28d1ee180310', '918f1140-2e57-4ed9-9a7b-030199f714c9');")
            execSQL("INSERT into PizzaIngredient (pizzaId, ingredientId) VALUES ('a6041e38-3a4b-4573-8e57-28d1ee180310', '6a31d0e0-9565-49ce-8684-0370ec587969');")
            execSQL("INSERT into PizzaIngredient (pizzaId, ingredientId) VALUES ('a6041e38-3a4b-4573-8e57-28d1ee180310', '6ad123c3-b7f6-43a2-8b66-3362382638b7');")
            execSQL("INSERT into PizzaIngredient (pizzaId, ingredientId) VALUES ('a6041e38-3a4b-4573-8e57-28d1ee180310', '29cb77d1-a837-4a61-aeb5-b79483716815');")
            execSQL("INSERT into PizzaIngredient (pizzaId, ingredientId) VALUES ('a6041e38-3a4b-4573-8e57-28d1ee180310', 'b843528b-d87f-4050-ae94-c0f532ca76b3');")
            execSQL("INSERT into PizzaIngredient (pizzaId, ingredientId) VALUES ('a6041e38-3a4b-4573-8e57-28d1ee180310', '54f12465-5df5-4c87-9de6-32a17de7c47f');")
            execSQL("INSERT into PizzaIngredient (pizzaId, ingredientId) VALUES ('a6041e38-3a4b-4573-8e57-28d1ee180310', '8147c56f-0072-4b46-8aaf-b762699c5ffb');")
            execSQL("INSERT into Drink (uid, name, price) VALUES ('c4c8ef1f-0de1-4686-a088-22e616945e8e', 'Still Water', 1.0);")
            execSQL("INSERT into Drink (uid, name, price) VALUES ('c76a19f7-0a01-4fda-8d0d-3eca233f89be', 'Sparkling Water', 1.5);")
        }
    }
}