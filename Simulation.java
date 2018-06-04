import java.util.Random;

public class Simulation {

    private int maxSpeed;
    private int lineCapacity;
    private int numOfCars;
    private int[][] highway;

    public Simulation(int speed, int lineCapacity, int numOfCars){
        this.maxSpeed = speed;
        this.lineCapacity = lineCapacity;
        this.numOfCars = numOfCars;
        this.highway = new int[2][lineCapacity];
        init(highway);
    }

    private void init(int[][] road){

        for(int i = 0; i < 2; i++){
            for(int j = 0; j < this.lineCapacity; j++){
                road[i][j] = -1;
            }
        }
    }


    private void addCars(int[][] road, int line){
        Random rand = new Random();
        int i = this.numOfCars;

        while(i != 0){
            int raNum = rand.nextInt(this.lineCapacity) + 1;
            try{
                if(road[line][raNum] == -1){
                    int raSpeed = rand.nextInt(this.maxSpeed) + 1;
                    road[line][raNum % this.lineCapacity] = raSpeed;
                    i--;
                }
            } catch(IndexOutOfBoundsException e){}

        }
    }

    private int getDistance(int row, int start, int speed){

        int distance = 0;
        for(int i = 1; i <= speed; i++){
            if(this.highway[row][(start + i) % this.lineCapacity] == -1){
                distance += 1;
            }else{
                break;
            }
        }
        return distance;
    }

    private boolean isAbleToMove(int row, int start, int speed){
        boolean isEmpty = true;
        for(int i = 1; i <= speed; i++){
            if(this.highway[row][(start + i) % this.lineCapacity] != -1){
                isEmpty = false;
            }
        }
        return isEmpty;
    }

    private boolean changeLines(int i, int currentSpeed){
        boolean change;

        int disA = getDistance(0, i, currentSpeed);
        int disB = getDistance(1, i, currentSpeed);
        change = (disA < disB);
        return change;
    }


    private void applyRules(int line, int randomLimit){
        int currentSpeed;
        Random rand = new Random();
        for(int i = 0; i < this.lineCapacity; i++){
            for(int j = 0; j < line; j++){
                currentSpeed = this.highway[j][i];

                // If there's a car
                if(currentSpeed != -1){

                    boolean isFreeToMove = isAbleToMove(j, i, currentSpeed);
                    boolean changeToB = false, changeToA = false;

                    // Accelerate
                    if(isFreeToMove && currentSpeed < this.maxSpeed){
                        this.highway[j][i] += 1;
                    }

                    // Slow Down
                    else if(!isFreeToMove && currentSpeed >= 1){
                        this.highway[j][i] -= 1;
                        // Line B is not occupied
                        if(j == 0 && this.highway[j+1][i] == -1){
                            changeToB = changeLines(i, currentSpeed);
                        }
                        // Line A is not occupied.
                        else if(j == 1 && this.highway[j-1][i] == -1){
                            changeToA = changeLines(i, currentSpeed);
                        }
                        if(changeToA || changeToB){
                            // convert to neg. To identify it as a switching car
                            this.highway[j][i] = -this.highway[j][i];
                        }

                    }

                    // Randomization
                    else if(rand.nextInt(randomLimit) + 1 < 3 && currentSpeed > 0){
                        this.highway[j][i] -= 1;
                        // Line B is not occupied
                        if(j == 0 && this.highway[j+1][i] == -1){
                            changeToB = changeLines(i, currentSpeed);
                        }
                        // Line A is not occupied.
                        else if(j == 1 && this.highway[j-1][i] == -1){
                            changeToA = changeLines(i, currentSpeed);
                        }
                        if(changeToA || changeToB){
                            // convert to neg. To identify it as a switching car
                            this.highway[j][i] = -this.highway[j][i];
                        }
                    }

                    // ------------------ Hacer regla para carros que estan pegados......
                    if(getDistance(j, i, currentSpeed) < 1){
                        //this.highway[j][i] = 0;
                    }
                }

            }
        }

        // Move cars
        int highway2[][] = new int[2][this.lineCapacity];
        init(highway2);
        for(int i = 0; i < this.lineCapacity; i++){
            for(int j = 0; j < line; j++){
                if(this.highway[j][i] != -1){
                    if(this.highway[j][i] < -1){
                        this.highway[j][i] -= this.highway[j][i] * 2;
                        if(j == 0){
                            highway2[1][(i + this.highway[j][i]) % this.lineCapacity] = this.highway[j][i];
                        }else{
                            highway2[0][(i + this.highway[j][i]) % this.lineCapacity] = this.highway[j][i];
                        }
                    }else{
                        highway2[j][(i + this.highway[j][i]) % this.lineCapacity] = this.highway[j][i];
                    }
                }
            }
        }
        this.highway = highway2;

    }

    // Main function
    public void run(int times, int rand){
        addCars(this.highway, 0);
        while (times != 0){
            applyRules(2, rand);
            printHighway(this.highway);
            System.out.println();
            times -= 1;
        }
    }


    public void printHighway(int[][] road){
        for(int i = 0; i < 2; i++){
            for(int j = 0; j < this.lineCapacity; j++){
                if(i == 0 && j == 0) System.out.print("A ->");
                if (i == 1 && j==0) System.out.print("B ->");
                if(road[i][j] == -1){
                    System.out.print(".");
                }else{
                    System.out.print(road[i][j]);
                }

            }
            System.out.println();

            for(int e = 0; e < this.lineCapacity + 4; e++){
                System.out.print("-");
            }
            System.out.println();
        }
    }

}
