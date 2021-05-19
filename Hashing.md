# Hashed Linked List
<details>
<summary>1. Hash Function</summary>


``` java
private long djb2Hash(String str) {
    long hash = 5381;
    for (int i = 0; i < str.length(); i++) {
        hash = str.charAt(i) + ((hash << 5) + hash);
    }
    return hash;
}
```

Hash function is to map data of arbitrary size to fixed-size values.
In this case, we want map data from string to integer.
***
</details>

<details>
<summary>2. Limiting Hash Function</summary>

``` java
private int limitHash(long val) {
    int id = (int) val % MAX_SIZE;
    return id < 0 ? id + MAX_SIZE : id;
}
```

The result from hash function must be fit to fixed-size bucket.
So, the result must be mod with the maximum size of bucket. 

The result from hash function can be negative because of data type overflow.
So, the result must be converted to positive.
***
</details>

<details>
<summary>3. Collision - Object Structure</summary>

``` java
int MAX_SIZE = 2
static class FoodType {
    String name;
    FoodType collision;

    FoodType(String name) {
        this.name = name;
        this.collision = null;
    }
}

private static FoodType[] foodTypeTable = new FoodType[MAX_SIZE];
```

Assume that we want to create a hash table for food type. 
To tackle the collision, we will use linked list.

``` java
private static FoodType searchFoodType(String searchName) {
    long hashNum = djb2Hash(searchName);
    int hashId = limitHash(hashNum);

    FoodType curFoodType = foodTypeTable[hashId];
    while (curFoodType != null) {
        if (curFoodType.name.equals(searchName)) {
            return curFoodType;
        }
        curFoodType = curFoodType.collision;
    }
    return null;
}
```
***
</details>

<details>
<summary>4. Collision - Insertion</summary>

This method is for inserting new food type
``` java
private static void insertFoodType(String foodTypeName) {
    FoodType newFoodType = new FoodType(foodTypeName);
    long hashNum = djb2Hash(newFoodType.name);
    int hashId = limitHash(hashNum);

    if (foodTypeTable[hashId] == null) {
        foodTypeTable[hashId] = newFoodType;
    } else {
        FoodType prevFoodType = null;
        FoodType curFoodType = foodTypeTable[hashId];

        while (curFoodType != null) {
            if (curFoodType.name.equals(newFoodType.name)) {
                break;
            }
            prevFoodType = curFoodType;
            curFoodType = curFoodType.collision;
        }
        
        if(curFoodType == null) {
            prevFoodType.collision = newFoodType;
        }
    }
}
```
***
</details>

<details>
<summary>5. Collision - Search</summary>

This method is for inserting new food type

``` java
private static FoodType searchFoodType(String searchName) {
    long hashNum = djb2Hash(searchName);
    int hashId = limitHash(hashNum);

    FoodType curFoodType = foodTypeTable[hashId];
    while (curFoodType != null) {
        if (curFoodType.name.equals(searchName)) {
            return curFoodType;
        }
        curFoodType = curFoodType.collision;
    }
    return null;
}
```
***
</details>

<details>
<summary>6. Nested Linked List - Object Structure</summary>

<table border="0">
<tr>
<td>From</td>
<td>To</td>
</tr>
<tr>
<td>

``` java














static class FoodType {
    String name;
    FoodType collision;
    

    FoodType(String name) {
        this.name = name;
        this.collision = null;
    }
}
```

</td>
<td>

``` java
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
```

</td>
</tr>
</table>


This method is for inserting new food type

***
</details>

<details>
<summary>7. Nested Linked List - Insertion</summary>

<table border="0">
<tr>
<td>From</td>
<td>To</td>
</tr>
<tr>
<td>

``` java



















private static void insertFoodType(String foodTypeName) {
    FoodType newFoodType = new FoodType(foodTypeName);
    
    long hashNum = djb2Hash(newFoodType.name);
    int hashId = limitHash(hashNum);

    if (foodTypeTable[hashId] == null) {
        foodTypeTable[hashId] = newFoodType;
        
    } else {
        FoodType prevFoodType = null;
        FoodType curFoodType = foodTypeTable[hashId];

        while (curFoodType != null) {
            if (curFoodType.name.equals(newFoodType.name)) {
            
                break;
            }

            prevFoodType = curFoodType;
            curFoodType = curFoodType.collision;
        }

        if(curFoodType == null) {
            prevFoodType.collision = newFoodType;
            
        }
    }
}
```

</td>
<td>

``` java
private static void insertFood(
    FoodType foodType, 
    Food newFood) {
    
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

private static void insertFoodType(
    String foodTypeName, 
    String foodName) {
    
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
```

</td>
</tr>
</table>


This method is for inserting new food type

***
</details>

### Main
``` java
public static void main(String[] args) {
    insertFoodType("Noodle", "Mie Aceh");       // 0
    insertFoodType("Rice", "Nasi Goreng");      // 0
    insertFoodType("Bread", "Roti Kukus");      // 1
    insertFoodType("Noodle", "Indomie");        // 0
    insertFoodType("Noodle", "Ramen");          // 0
    insertFoodType("Noodle", "Indomie");        // 0

    System.out.println(searchFoodType("Noodle", "Indomie").likes);
}
```