public class MMapProgram {
    public static void main(String[] args) {
    MMap<String,Integer> mmap = new MMap<>();
    System.out.println(mmap.size());
    System.out.println(mmap.isEmpty());
    System.out.println(mmap.put("Один",1));
    System.out.println(mmap.put("Три",3));
    System.out.println(mmap.put("Два",2));
    System.out.println(mmap.size());
    System.out.println(mmap.isEmpty());

    System.out.println("");

    System.out.println(mmap.containsKey("Четыре"));
    System.out.println(mmap.containsKey(3));
    System.out.println(mmap.containsKey("Три"));
    System.out.println(mmap.containsValue(4));
    System.out.println(mmap.containsValue(3));
    System.out.println(mmap.containsValue("Три"));

    System.out.println("");

    System.out.println(mmap.get("Три"));
    System.out.println(mmap.put("Три",2));
    System.out.println(mmap.replace("Три",3));
    System.out.println(mmap.get("Три"));

    System.out.println("");

    MMap<String,Integer> newMMap = new MMap<>(new String[]{"Три","Два","Один"}, new Integer[]{3,1,1});
    System.out.println(mmap.equals(1));
    System.out.println(mmap.equals(newMMap));
    System.out.println(newMMap.put("Два",2));
    System.out.println(mmap.equals(newMMap));
    newMMap.clear();
    System.out.println(newMMap.isEmpty());

    System.out.println("");

    System.out.println(mmap.getOrDefault("Три",-101));
    System.out.println(mmap.getOrDefault("-101",-101));
    System.out.println(mmap.remove("2"));
    System.out.println(mmap.remove("Два"));
    System.out.println(mmap.put("Два",2));
    System.out.println(mmap.remove("Два",3));
    System.out.println(mmap.get("Два"));
    System.out.println(mmap.remove("Два",2));

    System.out.println("");
    System.out.println(mmap.replace("Два",2,3));
    System.out.println(mmap.replace("Два",null,2));
    System.out.println(mmap.get("Два"));

    System.out.println("");

    System.out.println(mmap.remove("Три"));
    System.out.println(mmap.putIfAbsent("Два",2));
    System.out.println(mmap.putIfAbsent("Три",3));

    System.out.println("");

    MMap<String,Integer> t1 = new MMap<>(mmap);
    System.out.println(t1.size());
    System.out.println(t1.get("Один"));
    System.out.println(t1.get("Два"));
    System.out.println(t1.get("Три"));

    System.out.println("");

    MMap<String,Integer> t2 = new MMap<>(new String[]{"Один"},new Integer[]{1,2});
    System.out.println(t2.size());
    System.out.println(t2.get("Один"));
    }

}
