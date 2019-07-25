package Servers;

public class Test {
    public static void main(String[] args){
        Chinese c = new Chinese();
        System.out.println(c.getName());
    }
}
class Person{
    private String name = "Person";
   public String getName(){
       return name;
   }

}
class Chinese extends Person{

    public String getName(){
        String fromFather = super.getName();
        return fromFather + " + chinese";
    }


}
