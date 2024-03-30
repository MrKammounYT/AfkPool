package tn.Manager;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import tn.Run.GameRun;
import tn.afkpool.AFKPool;
import tn.utils.LocationAPI;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class GameManager {


    private final  int time;


    HashMap<Player,Integer> AFKPlayers = new HashMap<Player,Integer>();

    private final FileManager fileManager;

    private final ItemsManager itemsManager;

    private final LocationAPI locationAPI;

    
    private final int maxMoney;

    private final int minMoney;
    
    private World AFKPool_World;
    private Location AFKPool_Min;
    private Location AFKPool_Max;

    private final int MoneyPercentage;

    protected Random rd = new Random();;

    private final AFKPool main;
    private GameRun gameRun;




    public GameManager(AFKPool main){
        this.main = main;
        this.itemsManager = new ItemsManager();
        this.fileManager = new FileManager(itemsManager,main.getConfig());
        this.time = main.getConfig().getInt("afk-time");
        this.MoneyPercentage = main.getConfig().getInt("Money-Percentage");
        this.maxMoney = main.getConfig().getInt("max-money");
        this.minMoney = main.getConfig().getInt("min-money");
        this.locationAPI = new LocationAPI(main);
        if(locationAPI.LocationExists("pos1") && locationAPI.LocationExists("pos2")){
            Location loc1 = locationAPI.getLocation("pos1");
            Location loc2 = locationAPI.getLocation("pos2");
            AFKPool_World = loc1.getWorld();
            AFKPool_Min = new Location(loc1.getWorld(), Math.min(loc1.getX(), loc2.getX()), Math.min(loc1.getY(), loc2.getY()), Math.min(loc1.getZ(), loc2.getZ()));
            AFKPool_Max = new Location(loc1.getWorld(), Math.max(loc1.getX(), loc2.getX()), Math.max(loc1.getY(), loc2.getY()), Math.max(loc1.getZ(), loc2.getZ()));
            gameRun = new GameRun(this);
            gameRun.runTaskTimer(AFKPool.getInstance(),0,20);
        }else{
            main.getLogger().info("Please Setup your pos1 and pos2 in game");
        }
    }

    public void stopGameRun(){
        if(gameRun == null)return;
        gameRun.cancel();
    }
    public int getMoneyPercentage() {
        return MoneyPercentage;
    }

    public void createPlayer(Player p){
        AFKPlayers.put(p,time);
    }

    public void addRandomMoney(Player p){
        int x = rd.nextInt(minMoney,maxMoney);
        main.getEconomy().bankDeposit(p.getName(),x);
        p.sendMessage(AFKPool.Prefix + AFKPool.color("&aYou got Lucky and won &e"+x+"&a$"));

    }

    public HashMap<Player, Integer> getPlayers() {
        return AFKPlayers;
    }

    public int getTime() {
        return time;
    }
    public ItemsManager getItemsManager() {
        return itemsManager;
    }

    public World getAFKPool_World() {
        return AFKPool_World;
    }

    public boolean isInRegion(Player p){
        Location playerLoc = p.getLocation();
        return (playerLoc.getX() >=AFKPool_Min.getX() && playerLoc.getX() <= AFKPool_Max.getX())
                && (playerLoc.getY() >= AFKPool_Min.getY() && playerLoc.getY() <= AFKPool_Max.getY())
                &&( playerLoc.getZ() >= AFKPool_Min.getZ() && playerLoc.getZ() <= AFKPool_Max.getZ());


    }

    public LocationAPI getLocationAPI() {
        return locationAPI;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

}
