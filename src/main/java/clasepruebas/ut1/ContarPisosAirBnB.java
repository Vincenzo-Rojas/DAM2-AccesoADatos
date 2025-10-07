package clasepruebas.ut1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ContarPisosAirBnB {
    public static Boolean isNumeric(String text){
        int valor = 0;
        try{
            valor = Integer.parseInt(text);
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }
    public static void main(String[] args){

        Path rutaFichero = Path.of("src", "main", "java", "clasepruebas", "ut1", "listings.csv");
        HashMap<String, Integer> propietarios = new HashMap<>();
        BufferedReader br = null;
        try{
            br = new BufferedReader(new FileReader(rutaFichero.toFile()));
            String linea = "";
            br.readLine();
            int maxPisos = -1;
            String mayorTenedor = "";
            while((linea= br.readLine()) != null){
                String[] campos = linea.split(",");
                if(campos.length != 16){
                    continue;
                }
                int posicion = 1;
                do{
                    posicion++;
                }while(!isNumeric(campos[posicion]));
                if(!propietarios.containsKey(campos[posicion])){
                    propietarios.put(campos[posicion], 1);
                }else{
                    propietarios.put(campos[posicion], propietarios.get(campos[posicion]) + 1);
                    if(propietarios.get(campos[posicion]) > maxPisos){
                        maxPisos = propietarios.get(campos[posicion]);
                        mayorTenedor = campos[posicion];
                    }
                }
            }
            System.out.println(mayorTenedor + " --> " + maxPisos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
