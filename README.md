
## Escuela Colombiana de Ingeniería
### Arquitecturas de Software – ARSW
### INTEGRANTES: Juan Sebastian Cepeda Saray, Santiago Forero Yate


#### Ejercicio – programación concurrente, condiciones de carrera y sincronización de hilos. EJERCICIO INDIVIDUAL O EN PAREJAS.


##### Parte I – Antes de terminar la clase.

Control de hilos con wait/notify. Productor/consumidor.

1. Revise el funcionamiento del programa y ejecútelo. Mientras esto ocurren, ejecute jVisualVM y revise el consumo de CPU del proceso correspondiente. A qué se debe este consumo?, cual es la clase responsable?
     - El consumo se debe a que consumidor esta tratando de extraer lo que hay en la cola, y en la mayoria de los casos no hay nada en la cola, por lo que apenas 	el productor produce algo, de una vez el consumidor lo jala, haciendo asi que se consuma CPU por parte del programa en un 8%
         ![image](https://github.com/santiforero1018/LAB3_ARSW/assets/88952698/c17dd820-d92c-4e69-9a15-44957afb9c85)
	![image](https://github.com/santiforero1018/LAB3_ARSW/assets/88952698/c72c3898-04ef-45b7-b604-2d625f176818)
2. Haga los ajustes necesarios para que la solución use más eficientemente la CPU, teniendo en cuenta que -por ahora- la producción es lenta y el consumo es rápido. Verifique con JVisualVM que el consumo de CPU se reduzca.
3. Haga que ahora el productor produzca muy rápido, y el consumidor consuma lento. Teniendo en cuenta que el productor conoce un límite de Stock (cuantos elementos debería tener, a lo sumo en la cola), haga que dicho límite se respete. Revise el API de la colección usada como cola para ver cómo garantizar que dicho límite no se supere. Verifique que, al poner un límite pequeño para el 'stock', no haya consumo alto de CPU ni errores.
   	- Implementando las funcionalidades para reducir el consumo de CPU, se realiza lo que se pide, cumpliendo asi con el objetivo de manera satisfactoria
   	  ![image](https://github.com/santiforero1018/LAB3_ARSW/assets/88952698/b6e49276-a4d8-4067-8cfb-37182a6a2216)
   	  ![image](https://github.com/santiforero1018/LAB3_ARSW/assets/88952698/d401e788-e78f-4dbd-b2a6-d0596998ca1c)
	  ![image](https://github.com/santiforero1018/LAB3_ARSW/assets/88952698/79a305ac-0807-4c81-bc22-049e240e9a4a)
	- Basicamente, lo que se hace con estas implementaciones, es que el consumidor entre en un estado de espera si la cola esta vacia, y si la cola esta 		  llena, el productor entra en estado de espera, logrando asi una gran reducción en el consumo de la CPU.

##### Parte II. – Avance para el jueves, antes de clase.

Sincronización y Dead-Locks.

![](http://files.explosm.net/comics/Matt/Bummed-forever.png)

1. Revise el programa “highlander-simulator”, dispuesto en el paquete edu.eci.arsw.highlandersim. Este es un juego en el que:

	* Se tienen N jugadores inmortales.
	* Cada jugador conoce a los N-1 jugador restantes.
	* Cada jugador, permanentemente, ataca a algún otro inmortal. El que primero ataca le resta M puntos de vida a su contrincante, y aumenta en esta misma cantidad sus propios puntos de vida.
	* El juego podría nunca tener un único ganador. Lo más probable es que al final sólo queden dos, peleando indefinidamente quitando y sumando puntos de vida.

2. Revise el código e identifique cómo se implemento la funcionalidad antes indicada. Dada la intención del juego, un invariante debería ser que la sumatoria de los puntos de vida de todos los jugadores siempre sea el mismo(claro está, en un instante de tiempo en el que no esté en proceso una operación de incremento/reducción de tiempo). Para este caso, para N jugadores, cual debería ser este valor?.
	- El valor deberia ser N multiplicado por los puntos de vida iniciales del Immortal (DEFAULT_IMMORTAL_HEALTH) 

3. Ejecute la aplicación y verifique cómo funcionan las opción ‘pause and check’. Se cumple el invariante?.
	- Al implementar esta funcionalidad, inicialmente no se cumple el invariante, pues se presentan valores muy dispersos.
4. Una primera hipótesis para que se presente la condición de carrera para dicha función (pause and check), es que el programa consulta la lista cuyos valores va a imprimir, a la vez que otros hilos modifican sus valores. Para corregir esto, haga lo que sea necesario para que efectivamente, antes de imprimir los resultados actuales, se pausen todos los demás hilos. Adicionalmente, implemente la opción ‘resume’.

5. Verifique nuevamente el funcionamiento (haga clic muchas veces en el botón). Se cumple o no el invariante?.
	- Aunque se realiza la implementación correspondiente, sigue sin cumplirse el invarainte


6. Identifique posibles regiones críticas en lo que respecta a la pelea de los inmortales. Implemente una estrategia de bloqueo que evite las condiciones de carrera. Recuerde que si usted requiere usar dos o más ‘locks’ simultáneamente, puede usar bloques sincronizados anidados:

	```java
	synchronized(locka){
		synchronized(lockb){
			…
		}
	}
	```
	
 	- Para este caso, se identifico la siguiente región critica y se realizó la siguiente implementación:
	![image](https://github.com/santiforero1018/LAB3_ARSW/assets/88952698/a309dfb4-b3d2-49d5-b7ac-68453e9dadcb)

7. Tras implementar su estrategia, ponga a correr su programa, y ponga atención a si éste se llega a detener. Si es así, use los programas jps y jstack para identificar por qué el programa se detuvo.
	- El programa nunca llego a este estado, por lo que se pudo trabajar sin ningun problema los ultimos puntos
	
8. Plantee una estrategia para corregir el problema antes identificado (puede revisar de nuevo las páginas 206 y 207 de _Java Concurrency in Practice_).
	- ![image](https://github.com/santiforero1018/LAB3_ARSW/assets/88952698/a309dfb4-b3d2-49d5-b7ac-68453e9dadcb)
 	- Usando el operador ternario, se determina cual inmortal se bloquea primero y cual se bloquea despues para no entrar en un DeadLock	 
9. Una vez corregido el problema, rectifique que el programa siga funcionando de manera consistente cuando se ejecutan 100, 1000 o 10000 inmortales. Si en estos casos grandes se empieza a incumplir de nuevo el invariante, debe analizar lo realizado en el paso 4.
	- Con 10 hilos
	![image](https://github.com/santiforero1018/LAB3_ARSW/assets/88952698/c4674d07-d170-4be6-80c2-8abca22afb36)
	
 	- Con 100 inmortales
	![image](https://github.com/santiforero1018/LAB3_ARSW/assets/88952698/3f9c2687-c488-4c4e-82a4-36e9fdba4995)

 	- Con 1000 inmortales
	![image](https://github.com/santiforero1018/LAB3_ARSW/assets/88952698/ad06de8c-adba-4a05-85e8-d5c438936151)

10. Un elemento molesto para la simulación es que en cierto punto de la misma hay pocos 'inmortales' vivos realizando peleas fallidas con 'inmortales' ya muertos. Es necesario ir suprimiendo los inmortales muertos de la simulación a medida que van muriendo. Para esto:
	* Analizando el esquema de funcionamiento de la simulación, esto podría crear una condición de carrera? Implemente la funcionalidad, ejecute la simulación y observe qué problema se presenta cuando hay muchos 'inmortales' en la misma. Escriba sus conclusiones al respecto en el archivo RESPUESTAS.txt.
	* Corrija el problema anterior __SIN hacer uso de sincronización__, pues volver secuencial el acceso a la lista compartida de inmortales haría extremadamente lenta la simulación.

11. Para finalizar, implemente la opción STOP.
	- El siguiente codigo fue implementado para este boton:
	![image](https://github.com/santiforero1018/LAB3_ARSW/assets/88952698/6e3a0f77-0931-427d-b69f-2195411bcf07)

	![image](https://github.com/santiforero1018/LAB3_ARSW/assets/88952698/f45b0c2d-b33e-4e97-b2b0-d2d45aabedae)
  	

<!--
### Criterios de evaluación

1. Parte I.
	* Funcional: La simulación de producción/consumidor se ejecuta eficientemente (sin esperas activas).

2. Parte II. (Retomando el laboratorio 1)
	* Se modificó el ejercicio anterior para que los hilos llevaran conjuntamente (compartido) el número de ocurrencias encontradas, y se finalizaran y retornaran el valor en cuanto dicho número de ocurrencias fuera el esperado.
	* Se garantiza que no se den condiciones de carrera modificando el acceso concurrente al valor compartido (número de ocurrencias).


2. Parte III.
	* Diseño:
		- Coordinación de hilos:
			* Para pausar la pelea, se debe lograr que el hilo principal induzca a los otros a que se suspendan a sí mismos. Se debe también tener en cuenta que sólo se debe mostrar la sumatoria de los puntos de vida cuando se asegure que todos los hilos han sido suspendidos.
			* Si para lo anterior se recorre a todo el conjunto de hilos para ver su estado, se evalúa como R, por ser muy ineficiente.
			* Si para lo anterior los hilos manipulan un contador concurrentemente, pero lo hacen sin tener en cuenta que el incremento de un contador no es una operación atómica -es decir, que puede causar una condición de carrera- , se evalúa como R. En este caso se debería sincronizar el acceso, o usar tipos atómicos como AtomicInteger).

		- Consistencia ante la concurrencia
			* Para garantizar la consistencia en la pelea entre dos inmortales, se debe sincronizar el acceso a cualquier otra pelea que involucre a uno, al otro, o a los dos simultáneamente:
			* En los bloques anidados de sincronización requeridos para lo anterior, se debe garantizar que si los mismos locks son usados en dos peleas simultánemante, éstos será usados en el mismo orden para evitar deadlocks.
			* En caso de sincronizar el acceso a la pelea con un LOCK común, se evaluará como M, pues esto hace secuencial todas las peleas.
			* La lista de inmortales debe reducirse en la medida que éstos mueran, pero esta operación debe realizarse SIN sincronización, sino haciendo uso de una colección concurrente (no bloqueante).

	

	* Funcionalidad:
		* Se cumple con el invariante al usar la aplicación con 10, 100 o 1000 hilos.
		* La aplicación puede reanudar y finalizar(stop) su ejecución.
		
		-->

<a rel="license" href="http://creativecommons.org/licenses/by-nc/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by-nc/4.0/88x31.png" /></a><br />Este contenido hace parte del curso Arquitecturas de Software del programa de Ingeniería de Sistemas de la Escuela Colombiana de Ingeniería, y está licenciado como <a rel="license" href="http://creativecommons.org/licenses/by-nc/4.0/">Creative Commons Attribution-NonCommercial 4.0 International License</a>.
