package tn.Manager;

import org.bukkit.Location;
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

    HashMap<Player,Integer> players = new HashMap<Player,Integer>();

    private final FileManager fileManager;

    private final ItemsManager itemsManager;

    private final LocationAPI locationAPI;

    private final int maxMoney;

    private final int minMoney;
    private Location regionMin;
    private Location regionMax;

    private Location loc2;
    private String worldName;

    protected Random rd;

    private final AFKPool main;




    public GameManager(AFKPool main){
        this.rd = new Random();
        this.main = main;
        this.itemsManager = new ItemsManager();
        this.fileManager = new FileManager(itemsManager,main.getConfig());
        this.time = main.getConfig().getInt("afk-time");
        this.maxMoney = main.getConfig().getInt("max-money");
        this.minMoney = main.getConfig().getInt("min-money");
        this.locationAPI = new LocationAPI(main);
        if(locationAPI.LocationExists("pos1") && locationAPI.LocationExists("pos2")){
            Location loc1 = locationAPI.getLocation("pos1");
            Location loc2 = locationAPI.getLocation("pos2");
            worldName = Objects.requireNonNull(loc1.getWorld()).getName();
            regionMin = new Location(loc1.getWorld(), Math.min(loc1.getX(), loc2.getX()), Math.min(loc1.getY(), loc2.getY()), Math.min(loc1.getZ(), loc2.getZ()));
            regionMax = new Location(loc1.getWorld(), Math.max(loc1.getX(), loc2.getX()), Math.max(loc1.getY(), loc2.getY()), Math.max(loc1.getZ(), loc2.getZ()));
            new GameRun(this).runTaskTimer(AFKPool.getInstance(),0,20);
        }else{
            main.getLogger().info("Please Setup your pos1 and pos2 in game");
        }
    }
    public void createPlayer(Player p){
        players.put(p,time);
    }

    public void addRandomMoney(Player p){
        main.getEconomy().bankDeposit(p.getName(),rd.nextInt(minMoney,maxMoney));
    }

    public HashMap<Player, Integer> getPlayers() {
        return players;
    }

    public int getTime() {
        return time;
    }
    public ItemsManager getItemsManager() {
        return itemsManager;
    }

    public boolean isInRegion(Player p){

        Location playerLoc = p.getLocation();

        if (playerLoc.getX() >=regionMin.getX() && playerLoc.getX() <= regionMax.getX()
                && playerLoc.getY() >= regionMin.getY() && playerLoc.getY() <= regionMax.getY()
                && playerLoc.getZ() >= regionMin.getZ() && playerLoc.getZ() <= regionMax.getZ()) return true;

        return regionMin.getY() < playerLoc.getY() && playerLoc.getY() > regionMax.getY() && playerLoc.getZ() > regionMin.getZ() && playerLoc.getZ() < regionMax.getZ()
                && playerLoc.getX() > regionMin.getX() && playerLoc.getX() < regionMax.getX();

    }

    public LocationAPI getLocationAPI() {
        return locationAPI;
    }

    public FileManager getFileManager() {
        return fileManager;
    }

}
