import java.util.Scanner;
import java.util.Arrays;
import java.util.stream.Stream;
import java.util.HashMap;
import java.util.Map;

//Objetos
class Node<E>{
    E dato; //Recibe un dato principal
    Node next; //Guarda el siguiente nodo
    Node nextDer; //Un segundo nodo que se puede guardar
    int num; // Guarda un numero identificable
    String[] articulos; //Guarda articulos

    //Crea un nodo complejo que conecta a otro
    public Node(E dato){
        this.dato = dato;
        this.next = null;
    }
    //Crea un nodo simple
    public Node(E nombre, int prioridad){
        this.dato=nombre;
        this.num=prioridad;
    }
    //Crea un nodo con dos posibles hijos
    public Node(E nombre, int index, String[] newArticulo){
        this.dato = nombre;
        this.num = index;
        this.articulos = newArticulo;
        this.next = null;
        this.nextDer = null;
    }
    public void mostrar(){
        System.out.print("Nombre: "+dato+" con "+num);
    }

}
//Tickets !!
class Pilas{//LIFO
    int superficie;
    String[] dato;
    int capacity;
    ListasEnlazadas empleados;

    int rotacion=0;

    public Pilas(int capacity){
        this.capacity = capacity;
        this.dato = new String[capacity];
        this.superficie = -1;
    }
     public boolean isEmpty(){return superficie == -1;}
     public boolean isFull(){return superficie == capacity-1;}

      public void push(ListasEnlazadas empleados, String cliente){
        if(isFull()){
            System.out.println("Limite de tickets alcanzados");
            return;
        }
        if(empleados.isCabezaEmpty()){
            System.out.println("No hay empleados para atender");
            return;
        }
        dato[++superficie] = ("Cliente "+ cliente +" atendido por "+empleados.getName(this.rotacion));
        this.rotacion++;

     }

    public void pop(){
            if(isEmpty()){
                System.out.println("Sin ventas registradas");
                return; //Indicador de error
            }
            System.out.println(dato[superficie--]);
             return;
        }

    public void peek(){//Ultimo ticket
        if(isEmpty()){
            System.out.println("Sin ventas registradas.");
            return;//Error
        }
        System.out.println(dato[superficie]);
        return;
    }

    public void mostrar(){//ver todos lso tickets
        for(int i=superficie; i>=0; i--){
            System.out.println(dato[i]);
        } 
    }


}
//Inventario
class Cola<E>{//FIFO 
    Node frente;
    Node back;
    public Cola(){
        this.frente = null;
        this.back = null;
    }
    
    public void EnCola(E dato){
        Node nuevoNodo = new Node(dato);
        if(back==null){
            frente = back = nuevoNodo;
            return;
        }
        back.next = nuevoNodo; 
        back = nuevoNodo;
        return;
    }

    public void QuitaFrente(){
        if(frente == null){
            System.out.println("Sin inventario...");
            return;
        }
        Node valor = frente;
        System.out.println(valor.dato);
        frente = frente.next;
        if(frente == null){
            back=null;
            System.out.println("Inventario vaciado");
        }
        return;
    }

    public void mostrar(){
        System.out.println("--------Inventario--------");
        if(back == null){
            System.out.println("Inventario vacio");
            return;
        }
        Node lista = frente;
        System.out.println("-------------------------");
        while(lista != null){
            System.out.println("|\t"+lista.dato+"\t\t|");
            lista = lista.next;
        }
        System.out.println("-------------------------");
        return;
    }
}
//Fila de clienteslientes
class Monticulo{//Colas Priorizadas
    Node clientes[];
    int size;

    public Monticulo(){
        this.clientes = new Node[50];
        this.size = 0;
    }
//Si esta lleno o vario
    public boolean vacio(){return size==0;}
    public boolean lleno(){return size==clientes.length-1;}
//decimos la posicion de los hijos
    public int setPapa(int posicion){return posicion / 2;}
    public int setHijoDer(int qnPapa){return (2*qnPapa+1);}
    public int setHijoIzq(int qnPapa){return (2*qnPapa);}
//Juntamos a hijos y fathers
    public boolean hayHijoIzq(int Papa){return setHijoIzq(Papa) <= this.size;}
    public boolean hayHijoDer(int Papa){return setHijoDer(Papa) <= this.size;}
//cambiar al papa por los hijos
    public void bajarPapa(int indexPapa){
        if(hayHijoIzq(indexPapa)){
            int hermanoIzq = setHijoIzq(indexPapa);
            if(clientes[hermanoIzq].num < clientes[indexPapa].num){
                Node nombrePapa = clientes[indexPapa];
                Node nombreIzq = clientes[setHijoIzq(indexPapa)];
//Cambiamos los lugares
                clientes[hermanoIzq] = nombrePapa;
                clientes[indexPapa] = nombreIzq;
                if(hayHijoDer(indexPapa)){cambiarHermanos(indexPapa);}
                return;
            }
            if(hayHijoDer(indexPapa)){
                int hermanoDer = setHijoDer(indexPapa);
                if(clientes[hermanoDer].num < clientes[indexPapa].num){
                    Node nombrePapa = clientes[indexPapa];
                    Node nombreDer = clientes[hermanoDer];
                    clientes[indexPapa] = nombreDer;
                    clientes[hermanoDer] = nombrePapa;
                    cambiarHermanos(indexPapa);
                    return;
                }
            }
        }
    }
    //Cambiar hermanos
    public void cambiarHermanos(int indexPapa){
        int hermanoIzq = setHijoIzq(indexPapa);
        int hermanoDer = setHijoDer(indexPapa);
        if(clientes[hermanoIzq].num > clientes[hermanoDer].num){
            Node nombreDer = clientes[hermanoDer];
            Node nombreIzq = clientes[hermanoIzq];
            clientes[hermanoDer] = nombreIzq;
            clientes[hermanoIzq]= nombreDer;
            return;
        }
        return; 
    }
//Mover un numero hasta arriba
    public void flotar(int indexNuevoCliente){
        int indexPapa = setPapa(indexNuevoCliente);
        bajarPapa(indexPapa);
        if(size>2){
            for(int i = 2; i<size; i++){
                bajarPapa(setPapa(i));
            }
        }
    }
//Movieminentos basicos
    public void nuevoCliente(String nombre, int articulos){
        if(lleno()){System.out.println("Fila demasiado larga"); return;}
        //Creamos nuevo nodo
        Node nuevoCliente = new Node(nombre, articulos);
        //Agregamos al cliente en el index 1 si no hay clientes
        if(size == 0){
            clientes[++size]=nuevoCliente;
            System.out.println("Primer cliente a la fila\n");
            return;
        }
        //Agregamos cliente
        clientes[++size] = nuevoCliente;
        flotar(size);
    }
    public void mostrar(){
        if(size == 0){
            System.out.println("Vacio...");
        }
        for(int i = 1; i<=size; i++){
            System.out.println(clientes[i].dato+" tiene "+clientes[i].num+" articulos;");
        }
    }
}
//Empleados
class ListasEnlazadas<E>{//Lista enlazada circular
    Node cabeza;
    Node cola;
    public ListasEnlazadas(){
        this.cabeza=null;
        this.cola=null;
    }
    public void push(String nombre){
        if(cola==null){
            Node empleadoNuevo=new Node(nombre);
            this.cabeza=this.cola=empleadoNuevo;
            empleadoNuevo.next = cabeza;
            System.out.println("Empleado contratado");
            return;
        }
        Node empleadoNuevo = new Node(nombre);
        cola.next=empleadoNuevo;
        cola = empleadoNuevo;
        empleadoNuevo.next=cabeza;
        System.out.println("Empleado contratado");
    }
    public void EliminarPrimero(){
        if(isCabezaEmpty()){System.out.println("Sin empleados");return;}
        cabeza = cabeza.next;
        cola.next = cabeza;
        System.out.println("Despedido el primer empleado");
    }
    public void EliminarUltimoo(){
        if(isCabezaEmpty()){System.out.println("Sin empleados");return;}
        Node temporal = cabeza;
        while(temporal.next != cola){
            temporal = temporal.next;
        }
        cola = temporal;
        cola.next = cabeza;
        System.out.println("Despedido el ultimo empleado");
    }
    public void buscar(E nombre){
        if(isCabezaEmpty()){System.out.println("Sin empleados");return;}
        Node temporal = cabeza;
        do{
            if(temporal.dato.equals(nombre)){
                temporal=temporal.next;
                System.out.println("El siguiente empleado despues de "+nombre+" es "+temporal.dato);
                return;
            }
            temporal = temporal.next;
            //System.out.println("entro y checo a "+temporal.dato);
        }while(temporal!=cabeza);
        System.out.println("No se encontro a este empleado");
        return;
    }
    public void mostrar(){
        System.out.println("\n===Nombres y orden de empleados==\n");
        if(isCabezaEmpty()){System.out.println("Sin empleados");return;}
        Node temporal = cabeza;
        do{
            System.out.print(temporal.dato+" -> ");
            temporal=temporal.next;
        }while(temporal!=cabeza);
        System.out.println("¬");
    }
    public String getName(int rotacion){
        int iteracion = 0;
        Node temp = cabeza;
        do{
            temp = temp.next;
            iteracion++;
        }while(iteracion < rotacion);
        return temp.dato.toString();
    }
    public boolean isCabezaEmpty(){
        return cabeza == null;
    }
}
//Diccionario de empleados
class ArbolBinario{
    Node raiz;

    public ArbolBinario(){
        this.raiz = null;
    }
    //Agregamos un nuevo nodo al arbol
    public void agregarProveedor(Node raizActual, String proveedor, int index, String[] articulos){
        //Si la raiz esta vacia, la establecemos como raiz
        if(raiz == null){
            Node nuevoProveedor = new Node(proveedor, index, articulos);
            raiz = nuevoProveedor;
            return;
        }
        //Verificamos el nuevo index sea mayor
        if(raizActual.num > index){
            if(raizActual.next == null){ //Si llegamos al ultimo hijo izquierdo
               raizActual.next = new Node(proveedor, index, articulos);//Se agrega
                return; 
            }
            agregarProveedor(raizActual.next, proveedor, index, articulos);//Exploramos el proximo hijo izquierdo
        //Verificamos que el index sea menor  
        }else if( raizActual.num < index){
            if(raizActual.nextDer == null){//Que no haya hijo derecho
                raizActual.nextDer = new Node(proveedor, index, articulos);
                return;
            }
            agregarProveedor(raizActual.nextDer, proveedor, index, articulos);//Exploramos el proximo hijo derecho
        }
    }
    //Buscar al num pequenno del lado izquierdo y regresar al papa
     public Node sucesorPequenno(Node indexActual){
        Node papa = indexActual;
        while(indexActual.next != null){
            papa = indexActual;
            indexActual = indexActual.next;
        }
        return papa;
    }
    //Buscar al num grande del lado derecho y regresar al papa
     public Node sucesorGrande(Node indexActual){
        Node papa = indexActual;
        while(indexActual.nextDer != null){
            papa = indexActual;
            indexActual = indexActual.nextDer; 
        }
        return papa;
    }
    //Eliminar un elemento
    public void eliminarProveedor(Node indexAnterior, Node indexActual, int num){
        //Pasamos por todos los index y no se encontro
        if(indexActual == null){
            System.out.println("---El proveedor no existe---");
            return;
        }
        //El index es menor
        if(indexActual.num > num){eliminarProveedor(indexActual, indexActual.next, num);return;}
        //El index es mayor
        if(indexActual.num < num){eliminarProveedor(indexActual, indexActual.nextDer, num); return;
        //El index es igual
        }else{
            if(indexActual == raiz){
                //pero es la raiz
                if(raiz.next == null && raiz.nextDer == null){raiz=null;return;} //No tiene hijos

                //Verificamos que haya hijo derecho
                if(indexActual.nextDer != null){
                    Node papaPeque = sucesorPequenno(indexActual.nextDer);
                    Node nuevoNodo = papaPeque;
                    Node viejoNodo = indexActual;
                    this.raiz = nuevoNodo;                    
                    if(viejoNodo.next != null){
                        this.raiz.next = viejoNodo.next;
                    }
                    if(viejoNodo.nextDer != this.raiz){
                        this.raiz.nextDer = viejoNodo.nextDer;
                        papaPeque.next = null;
                    }
                    preorden(raiz);
                    return;
                //Si solo hay hijo izquierdo                
                }else{
                    //Buscamos al papa del nodo mas grande
                    Node papaGrande = sucesorGrande(indexActual.next);
                    Node nuevoNodo = papaGrande.nextDer;
                    Node viejoNodo = indexActual;
                    //actualizamos la raiz a este nuevo nodo
                    this.raiz = nuevoNodo;
                    //Quitamos la referencia del padre
                    if(viejoNodo.next != this.raiz){
                        this.raiz.next = viejoNodo.next;
                        papaGrande.nextDer = null;
                        
                    }
                    preorden(raiz);
                    return;
                }
            }
            //Caso 1: Nodo hoja
            else if(indexActual.next == null && indexActual.nextDer == null){
                //Si es el hijo izquierdo
                if(indexAnterior.next == indexActual){
                    indexAnterior.next = null; 
                    return;
                }else{ //Si es el hijo derecho
                    indexAnterior.nextDer = null; 
                    return;
                }
            }
            //Caso 2: Nodo con un hijo izquierdo
            else if(indexActual.next != null && indexActual.nextDer == null){
                //Es el hijo izquierdo 
                if(indexActual.next == indexActual){
                    indexAnterior.next = indexActual.next; 
                    return;
                }else{ //El hijo esta a la derecha
                    indexAnterior.nextDer = indexActual.next;
                    return;
                }
            //Caso 2: Nodo con un hijo derecho
            }else if(indexActual.next == null && indexActual.nextDer!=null){
                //El nieto esta a la izquierda
            System.out.println("hijo derecho");
                if(indexAnterior.next == indexActual){
                    indexAnterior.next = indexActual.nextDer; 
                    return;
                }else{ //El nieto esta a la derecha
                    indexAnterior.nextDer = indexActual.nextDer;
                    return;
                }
            //Caso 3: Nodo con dos hijos
            }else{
                //Verificamos que haya hijo derecho
                if(indexActual.nextDer != null){
                    //Buscamos el num pequennoy guardamos el anterior nodo
                    Node papaPequeno = sucesorPequenno(indexActual.nextDer); //buscamos al papa del ultimo nodo
                    Node nuevoNodo = papaPequeno.next;
                    Node viejoNodo = indexActual;

                    //El viejo nodo esta a la izquierda de papa
                    if(indexAnterior.next == viejoNodo)indexAnterior.next = nuevoNodo;
                    //El viejo nodo esta a la derecha de papa
                    else indexAnterior.nextDer = nuevoNodo;

                    //Actualizamos los hijos del nuevo nodo
                    if(viejoNodo.next != null){
                        nuevoNodo.next = viejoNodo.next;
                    }
                    //Cuidamos que no sea un hijo directo
                    if(viejoNodo.nextDer!=nuevoNodo){
                        nuevoNodo.nextDer= viejoNodo.nextDer;
                        papaPequeno.next = null;
                    }
                    return;
                //Subimos al hijo mas grande izq
                }else{
                    Node papa = sucesorGrande(indexActual.next);
                    Node nuevoNodo = papa.nextDer;
                    Node viejoNodo = indexActual;
                    //Buscamos el No mas grande de 

                    //Verificamos que el viejo nodo este del izquierdo
                    if(indexAnterior.next == viejoNodo) indexAnterior.next = nuevoNodo;//actualizamos
                    //Esta en el lado derecho
                    else indexAnterior.nextDer = nuevoNodo;//actualizamos

                    if(viejoNodo.next != nuevoNodo){
                        nuevoNodo.next = viejoNodo.next;
                        papa.nextDer = null;
                    }
                }
            }
        }
    }
    public void preorden(Node proveedor){
        if(raiz==null){System.out.println("No hay proveedores");return;}
        System.out.println("\t"+proveedor.num+": "+proveedor.dato+" => "+proveedor.articulos[0]+", "+proveedor.articulos[1]);
        if(proveedor.next!= null)preorden(proveedor.next);
        if(proveedor.nextDer!=null)preorden(proveedor.nextDer);
    }
    public void inorden(Node proveedor){
        if(raiz==null){System.out.println("No hay proveedores");return;}
        if(proveedor.next!= null)preorden(proveedor.next);
        System.out.println("\t"+proveedor.num+": "+proveedor.dato+" => "+proveedor.articulos[0]+", "+proveedor.articulos[1]);
        if(proveedor.nextDer!=null)preorden(proveedor.nextDer);
    }
    public void postorden(Node proveedor){
        if(raiz==null){System.out.println("No hay proveedores");return;}
        if(proveedor.next!= null)preorden(proveedor.next);
        if(proveedor.nextDer!=null)preorden(proveedor.nextDer);
        System.out.println("\t"+proveedor.num+": "+proveedor.dato+" => "+proveedor.articulos[0]+", "+proveedor.articulos[1]);
    }
    public void buscarProveedor(int x, Node proveedorActual){
        //Primer caso base, no existe
        if(proveedorActual == null){System.out.println("No existe este index"); return;}
        //Buscamos en modo postOrden
        if(proveedorActual.next!= null)buscarProveedor(x, proveedorActual.next);
        if(proveedorActual.nextDer!=null)buscarProveedor(x, proveedorActual.nextDer);
        if(proveedorActual.num == x){
            System.out.println("\t"+proveedorActual.num+": "+proveedorActual.dato+" => "+proveedorActual.articulos[0]+", "+proveedorActual.articulos[1]);
            return;
        }
    }
}
//Ordnamiento y busqueda
class Ordenamiento{//Lista de cosas que faltan en la ferreteria por prioridad
    String[][] arreglo;
    int size;
    public Ordenamiento(){//Constructor
        this.size = -1;
        this.arreglo = new String[50][2];
    }
    public void burbuja(){//Ordenamiento por burbuja
        for(int i = 0; i<size; i++){
            for(int j = 0; j< size-1; j++){//Cuida los ciclos que no pase los que ya flotaron
                int par1 = Integer.parseInt(arreglo[j][0]);//Guardamos la prioridad
                int par2 = Integer.parseInt(arreglo[j+1][0]);//Guardamos la priodidad
                if(par1 > par2){
                    String[] numTemp = arreglo[j];
                    arreglo[j]=arreglo[j+1];
                    arreglo[j+1]=numTemp;
                }
            }
        }
        mostrar();
    }
    public void agregar(String articulo, String prioridad){
        ++size;
        this.arreglo[size][0] = prioridad;
        this.arreglo[size][1] = articulo;
        burbuja();
    }
    public void mostrar(){
        if(size == -1){System.out.println("--Sin pendientes"); return;}
        for(int i=0; i<size; i++){
            System.out.println(arreglo[i][0]+": "+arreglo[i][1]);
        }
    }
}

class hashAlmacen {
    // La Tabla Hash donde la Clave es el nombre del material (String) 
    // y el Valor es la ubicación en el almacén (String).
    private Map<String, String> ubicacionesMateriales;

    public hashAlmacen() {
        // Inicializa el HashMap
        this.ubicacionesMateriales = new HashMap<>();
    }
    public void agregarUbicacion(String material, String ubicacion) {
        // El método put() inserta el par clave-valor en la tabla hash.
        ubicacionesMateriales.put(material.toUpperCase(), ubicacion);
    }
    public String consultarUbicacion(String material) {
        String materialKey = material.toUpperCase();
        
        // El método get() accede directamente al valor usando la clave.
        // Esto es lo que proporciona la velocidad O(1) de la tabla hash.
        if (ubicacionesMateriales.containsKey(materialKey)) {
            return "Ubicación de " + material + ": " + ubicacionesMateriales.get(materialKey);
        } else {
            return "⚠️ Material \"" + material + "\" no encontrado en el sistema.";
        }
    }
}
//Main
class Main{
    //Hacer entregas pequennas
    //Dividimos un arreglo de todas las entregas que se haran
    //Se entrega primero los mas largos
    public static String[] recursividad(String[] entregas1) {
        if (entregas1.length <= 1) {
            return entregas1;
        }

        int mitad = entregas1.length / 2;
        
        // 1. División recursiva
        // Se llama a entregas, que devuelve un sub-arreglo ORDENADO.
        String[] primerGrupoOrdenado = recursividad(Arrays.copyOfRange(entregas1, 0, mitad));
        String[] segundoGrupoOrdenado = recursividad(Arrays.copyOfRange(entregas1, mitad, entregas1.length));

        // 2. Fusión (Merge)
        // Se fusionan los dos sub-arreglos ordenados y se devuelve el resultado.
        return merge(primerGrupoOrdenado, segundoGrupoOrdenado);
    }
    // Método de Fusión (Merge) que hace la mayor parte del trabajo de ordenamiento
    private static String[] merge(String[] arr1, String[] arr2) {
        int i = 0, j = 0, k = 0; // i para arr1, j para arr2, k para el resultado
        String[] resultado = new String[arr1.length + arr2.length];

        // Mientras haya elementos en ambos arreglos...
        while (i < arr1.length && j < arr2.length) {
            // Comparamos longitudes. Queremos el MÁS LARGO primero (orden descendente).
            if (arr1[i].length() >= arr2[j].length()) { 
                resultado[k++] = arr1[i++];
            } else {
                resultado[k++] = arr2[j++];
            }
        }
        // Copiar los elementos restantes de arr1 (si los hay)
        while (i < arr1.length) {
            resultado[k++] = arr1[i++];
        }
        // Copiar los elementos restantes de arr2 (si los hay)
        while (j < arr2.length) {
            resultado[k++] = arr2[j++];
        }
        return resultado;
    }
//mostramos
    public static void mostrarPedidos(String[] entregas){
        for(int iter = 0; iter < entregas.length; iter++){
            System.out.print(entregas[iter]+", ");
        }
    }
//Acomodar las rutas del grafo
    public static float[][] acomodorar(float[][] grafo){
        for(int intermedio=0; intermedio<grafo.length; intermedio++){
            for(int oriInicio = 0; oriInicio<grafo.length;oriInicio++){
                for(int oriFinal=0; oriFinal<grafo.length; oriFinal++){
                    if(grafo[oriInicio][intermedio] + grafo[intermedio][oriInicio] < grafo[oriInicio][oriFinal] && grafo[oriInicio][intermedio]!=0){
                        grafo[oriInicio][oriFinal] = grafo[oriInicio][intermedio] + grafo[intermedio][oriInicio];
                    }
                }
            }
        }
        return grafo;
    }
    public static void main(String[] args){
        Scanner read = new Scanner(System.in);
        //Crear objetos
        Cola inventario = new Cola();
        //Fila de los clientes
        Monticulo fila = new Monticulo();
        //Lista de empleados
        ListasEnlazadas empleados = new ListasEnlazadas();
        //tiquets
        Pilas tickets = new Pilas(50);
        //Proveedores
        ArbolBinario proveedores = new ArbolBinario();
        //Cajas
        Ordenamiento cajas = new Ordenamiento();
        //almacen
        hashAlmacen almacen = new hashAlmacen();
        //Memoria de la anterior mejor ruta
        float[][] grafo =null;
        //menu
        boolean menuPrincipal = true;
        while(menuPrincipal){
            System.out.println("\n\n===Ferreteria Luca's Son=== \n"+
                            "1. Inventario.\n"+
                            "2. Cliente en fila\n"+
                            "3. Empleados\n"+
                            "4. Imprimir ticket\n"+
                            "5. Proveedores\n"+
                            "6. Entrega compleja\n"+
                            "7. tablas hash\n"+
                            "8. Lista de compra\n"+
                            "9. Mapa\n"+
                            "10. Salir\n");
            int menu = read.nextInt(); read.nextLine();
            switch(menu){
                case 1:
                    System.out.println("\n====INVENTARIO===\n"+
                                        "1. Agregar articulo.\n"+
                                        "2. Sacar articulo\n"+
                                        "3. Ver articulos \n");
                    int menuInventario = read.nextInt(); read.nextLine();
                    switch (menuInventario) {
                        case 1:
                            System.out.print("Ingrese lo que quiere agregar:\n=>");
                            String articulo = read.nextLine();
                            inventario.EnCola(articulo);
                            System.out.println("---Articulo agregado");
                            
                            break;
                        case 2:
                            System.out.println("Se botara el articulo viejo");
                            System.out.print("Tirando: ");
                            inventario.QuitaFrente();
                            break;

                        case 3:
                            inventario.mostrar();
                            break;

                        default:
                            System.out.println("Dato invalido");
                            break;
                    }  
                    break;
                case 2:
                    System.out.println("\n===CLIENTES===\n"+
                                        "1. Se formo un cliente\n"+
                                        "2. Ver la fila");
                    int menuCliente = read.nextInt(); read.nextLine();
                    switch(menuCliente){
                        case 1:
                            System.out.print("Si se ha formado un cliente, ingrese su nombre:\n=>");
                            String nombre = read.nextLine();
                            System.out.print("Cantidad de articulos que comprara:\n=>" ); 
                            int articulos = read.nextInt(); read.nextLine();
                            fila.nuevoCliente(nombre, articulos);
                            tickets.push(empleados, nombre);
                            break;
                        case 2:
                            System.out.println("===Clientes===");
                            fila.mostrar();
                    }
                    break;
                case 3:
                   System.out.print("\n===EMPLEADOS===\n"+
                                        "1. Contratar empleado\n"+
                                        "2. Despedir primer empleado\n"+
                                        "3. Despedir ultimo empleado\n"+
                                        "4. Ver todos los empleados\n"+
                                        "5. Empleado siguiente\n"+
                                        "=>");
                    int menuEmpleado = read.nextInt(); read.nextLine();
                    switch (menuEmpleado) {
                        case 1:
                            System.out.print("Ingrese el nombre del empleado: \n=>>");
                            String nombre = read.nextLine();
                            empleados.push(nombre);
                            break;
                        case 2:
                            empleados.EliminarPrimero();
                            break;
                        case 3:
                            empleados.EliminarUltimoo();
                            break;
                        case 4:
                            empleados.mostrar();
                            break;
                        case 5:
                            System.out.print("Ingrese el nombre de un empleado: \n=>>");
                            nombre=read.nextLine();
                            empleados.buscar(nombre);
                            break;
                        default:
                            System.out.println("Opcion invalida");
                            break;
                    }
                    break;
                case 4:
                    System.out.println("\n===TICKETS===\n"+
                                    "1. Imprimir ultimo ticket.\n"+
                                    "2. Eliminar ticket.\n"+
                                    "3. Imprimir todos los tickets");
                    int menuTickets = read.nextInt();read.nextLine();
                    switch (menuTickets) {
                        case 1:
                            System.out.print("Ultimo cliente \n=>");
                            tickets.peek();
                            break;
                        case 2:
                            System.out.println("===Eliminar el ultimo ticket===");
                            tickets.pop();
                            break;
                        case 3:
                            System.out.println("\n===TICKETS===");
                            tickets.mostrar();
                            break;
                        default:
                            System.out.println("opcion invalida");
                            break;
                    }
                    break;
                case 5: //Menu de proveedores
                    System.out.print("\n----Proveedores----\n"+
                                        "1. Ver proveedores\n"+
                                        "2. Ingresar proveedor\n"+
                                        "3. Eliminar proveedor por index\n"+
                                        "4. Buscar proveedor por index\n"+
                                        "=>");
                    int menuProveedores = read.nextInt();read.nextLine();
                    switch(menuProveedores){
                        case 1://Mostrar proveedores
                            System.out.println("---Inorden---");
                            proveedores.inorden(proveedores.raiz);
                            System.out.println("---Postorden---");
                            proveedores.postorden(proveedores.raiz);
                            System.out.println("---Preorden---");
                            proveedores.preorden(proveedores.raiz);
                            break;
                        case 2://Agregar proveedor
                            System.out.print("Ingrese el nombre: "); String nombreProveedor = read.nextLine();
                            System.out.print("Ingrese el index: "); int numProveedor = read.nextInt(); read.nextLine();
                            String[] articulosProveedor = new String[2];
                            System.out.print("Ingrese 2 articulos que provea: ");articulosProveedor[0] =read.nextLine();
                            System.out.print("=> "); articulosProveedor[1] = read.nextLine();
                            proveedores.agregarProveedor(proveedores.raiz, nombreProveedor, numProveedor, articulosProveedor);
                            break;
                        case 3://Eliminar proveedor
                            System.out.print("==Elimnar proveedor por index===\n"+
                                                "Ingrese el index: ");
                            int indexProveedor = read.nextInt();read.nextLine(); 
                            proveedores.eliminarProveedor(null, proveedores.raiz, indexProveedor);
                            break;
                        case 4://Buscar proveedor
                            System.out.print("==Buscar proveedor por index===\n"+
                                                "Ingrese el index: ");
                            indexProveedor = read.nextInt();read.nextLine(); 
                            proveedores.buscarProveedor(indexProveedor, proveedores.raiz);
                    }
                    break;
                case 6://Entrega compleja - Recursividad
                //Solicitamos los datos
                    System.out.print("\n---Division de entregas complejas---\n"+
                                        "Ingrese cuantos pedidos son: ");
                    int pedidos = read.nextInt();read.nextLine();
                    String[] entregas = new String[pedidos];
                    System.out.println("Ingrese lo que entregara");
                    for(int iter = 0; iter<entregas.length; iter++){
                        System.out.print("=>");
                        entregas[iter]=read.nextLine();
                    }

                //Ordenamos los datos
                    System.out.println("El problema acomodado");
                    mostrarPedidos(recursividad(entregas));
                    break;
                case 7://Tablas hash
                    System.out.print("\n---Almacen---"+
                                    "\n1. Agregar ubicacion"+
                                    "\n2.buscar ubicacion\n=>");
                    int menuHash = read.nextInt();read.nextLine();
                    switch(menuHash){
                        case 1:
                            System.out.print("Ingrese los tipos de herramientas(Ejem: Carpi)");
                            String material = read.nextLine();
                            System.out.print("Ingrese una breve descripcion de donde esta\n=>");
                            String ubicacion = read.nextLine();
                            almacen.agregarUbicacion(material, ubicacion);
                            break;
                        case 2:
                            System.out.print("Ingrese que tipos de materiales busca.(Ejem: Carpinteria)\n=>");
                            material = read.nextLine();
                            System.out.println(almacen.consultarUbicacion(material));
                            break;
                    }
                    break;
                case 8: //Cajas
                    System.out.print("\n---Lista de cosas a comprar---\n"+
                                    "1. Mostrar\n"+
                                    "2. Agregar a la fila\n"+
                                    "=>");
                    int menuCajas = read.nextInt(); read.nextLine();
                    switch (menuCajas) {
                        case 1:
                            System.out.println("\n---LISTA---");
                            cajas.mostrar();
                            break;
                        case 2:
                            System.out.print("\tIngrese el articulo:\n\t=>");
                            String articulo = read.nextLine();
                            System.out.print("\tIngrese la prioridad\n\t=>");
                            int priori = read.nextInt(); read.nextLine();

                            String prioridad = Integer.toString(priori);
                            cajas.agregar(articulo, prioridad);
                            break;
                        default:
                            break;
                    }
                    
                    break;
                case 9: //Mapa
                    System.out.print("---RUTAS DE CAMIONES---\n"+
                                        "1.Ver ruta anterior\n"+
                                        "2. Nueva ruta\n"+
                                        "=>");
                    int menuMapa = read.nextInt();
                    switch (menuMapa) {
                        case 2:
                            System.out.println("¿Cuantos puntos de parada son?");
                            int paradas = read.nextInt();
                            grafo = new float[paradas][paradas];
                            for(int nodos = 0; nodos<grafo.length; nodos++){
                                for(int i=0; i<grafo.length; i++){
                                    System.out.println("El punto "+(nodos+1)+" tiene camina al punto "+(i+1)+
                                                        "\n(No) Ingrese 0. (Si) Ingrese los kilometros");
                                    grafo[nodos][i] = read.nextFloat();
                                    System.out.println(grafo[nodos][i]);
                                }
                            }
                            grafo = acomodorar(grafo);
                        break;
                        case 1:
                            if(grafo == null){
                                System.out.println("Sin mapas");
                            }else{
                                for(int i=0; i<grafo.length; i++){
                                    for(int x = 0; x<grafo.length; x++){
                                        System.out.print(grafo[i][x]+"\t");
                                    }
                                    System.out.println();
                                }
                            }
                            break;
                        default:
                            System.out.println("Opcion no reconocida");
                            break;
                    }                    
                    
                    break;
                case 10:
                    System.out.println("Saliendo");
                    menuPrincipal = false;
                    break;
                default:
                    System.out.println("Dato invalido");
                    break;
        }
    }
    }
}