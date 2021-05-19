class Hashing {
    // Hash function section
    private static int MAX_SIZE = 2;

    private static long djb2Hash(String str) {
        long hash = 5381;
        for (int i = 0; i < str.length(); i++) {
            hash = str.charAt(i) + ((hash << 5) + hash);
        }
        return hash;
    }

    private static int limitHash(long val) {
        int id = (int) val % MAX_SIZE;
        return id < 0 ? id + MAX_SIZE : id;
    }

    // Hash table section
    static class Food {
        String name;
        int likes;
        Food nextFood;

        Food(String name) {
            this.name = name;
            this.likes = 1;
            this.nextFood = null;
        }
    }

    static class FoodType {
        String name;
        FoodType collision;
        Food nextFood;

        FoodType(String name) {
            this.name = name;
            this.collision = null;
        }
    }

    private static FoodType[] foodTypeTable = new FoodType[MAX_SIZE];

    private static void insertFood(FoodType foodType, Food newFood) {
        Food prevFood = null;
        Food curFood = foodType.nextFood;

        while (curFood != null) {
            if (curFood.name.equals(newFood.name)) {
                curFood.likes += 1;
                break;
            }

            prevFood = curFood;
            curFood = curFood.nextFood;
        }

        if(curFood == null) {
            prevFood.nextFood = newFood;
        }
    }

    private static void insertFoodType(String foodTypeName, String foodName) {
        FoodType newFoodType = new FoodType(foodTypeName);
        Food newFood = new Food(foodName);
        long hashNum = djb2Hash(newFoodType.name);
        int hashId = limitHash(hashNum);

        if (foodTypeTable[hashId] == null) {
            foodTypeTable[hashId] = newFoodType;
            newFoodType.nextFood = newFood;
        } else {
            FoodType prevFoodType = null;
            FoodType curFoodType = foodTypeTable[hashId];

            while (curFoodType != null) {
                if (curFoodType.name.equals(newFoodType.name)) {
                    insertFood(curFoodType, newFood);
                    break;
                }

                prevFoodType = curFoodType;
                curFoodType = curFoodType.collision;
            }

            if(curFoodType == null) {
                prevFoodType.collision = newFoodType;
                newFoodType.nextFood = newFood;
            }
        }
    }

    private static Food searchFood(FoodType foodType, String searchFoodName) {
        Food curFood = foodType.nextFood;
        while (curFood != null) {
            if (curFood.name.equals(searchFoodName)) {
                return curFood;
            }
            curFood = curFood.nextFood;
        }
        return null;
    }

    private static Food searchFoodType(String searchFoodTypeName, String searchFoodName) {
        long hashNum = djb2Hash(searchFoodTypeName);
        int hashId = limitHash(hashNum);

        FoodType curFoodType = foodTypeTable[hashId];
        while (curFoodType != null) {
            if (curFoodType.name.equals(searchFoodTypeName)) {
                return searchFood(curFoodType, searchFoodName);
            }
            curFoodType = curFoodType.collision;
        }
        return null;
    }

    public static void main(String[] args) {
        insertFoodType("Noodle", "Mie Aceh");       // 0
        insertFoodType("Rice", "Nasi Goreng");      // 0
        insertFoodType("Bread", "Roti Kukus");      // 1
        insertFoodType("Noodle", "Indomie");        // 0
        insertFoodType("Noodle", "Ramen");          // 0
        insertFoodType("Noodle", "Indomie");        // 0

        System.out.println(searchFoodType("Noodle", "Indomie").likes);
    }
}