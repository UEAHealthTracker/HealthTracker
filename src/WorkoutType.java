public enum WorkoutType {

    //exercise types- MET value for each type based on information at https://golf.procon.org/met-values-for-800-activities/

    AEROBICS(6),
    BASKETBALL(8),
    BOXING(12),
    CRICKET(5),
    CIRCUIT_TRAINING(8),
    CYCLING(7),
    DANCING(5),
    FOOTBALL(8),
    GYMNASTICS(6),
    HIKING(7),
    HOCKEY(8),
    HORSE_RIDING(5),
    MOUNTAIN_BIKING(9),
    MARTIAL_ARTS(10),
    PILATES(3),
    RUNNING(8),
    ROCK_CLIMBING(8),
    ROWING(6),
    RUGBY(8),
    SKATING(7),
    SKIING(7),
    SKIPPING(12),
    SWIMMING(7),
    TENNIS(7),
    WALKING(3),
    WEIGHT_LIFTING(5),
    YOGA(3);

    //MET = metabolic equivalent for task- it is based on the intensity and used to calculate calories burnt
    public int MET;

    //default constructor for enum
    WorkoutType(int MET){
        this.MET = MET;
    }
}
