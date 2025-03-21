📌 Funcionalidades añadidas:

Implementación de stringResource: Ahora todos los textos visibles en la interfaz se obtienen de los archivos de recursos (strings.xml), facilitando la internacionalización y el soporte multilingüe (español, inglés y catalán).

Optimización del ViewModel: Se ha mejorado la lógica de gestión de datos de la lista de viajes (travelItems) para asegurar un flujo de datos más reactivo y eficiente utilizando StateFlow.

Refactorización del código: Se ha reorganizado y limpiado el código para mejorar la legibilidad, modularidad y facilitar el mantenimiento.

Mejora de la pantalla Home: Se han reestructurado los botones de acción (Agregar, Guardar, Cancelar) para que sean más intuitivos, adaptables y accesibles visualmente.

Persistencia tras rotación de pantalla: Se ha gestionado correctamente el estado de los elementos (lista de viajes, idioma seleccionado, etc.) para que no se pierda al rotar el dispositivo.

Implementación de logs: Se han incorporado Log.i y Log.e para facilitar la depuración y seguimiento del comportamiento de la app en eventos clave: inicio de sesión, errores, cambio de idioma, adición/eliminación de viajes.

Inyección de dependencias usando Hilt: Se ha incorporado Hilt para una gestión eficiente y escalable de las dependencias del proyecto.


✅ Descripción de los Tests del TravelListViewModel:
Los tests unitarios aseguran que el comportamiento del TravelListViewModel sea correcto. A continuación, se describe el propósito de cada uno:

addTravelItem adds a new item
Verifica que al agregar un nuevo viaje, este se añada correctamente a la lista del ViewModel.

deleteTravelItem removes the item
Comprueba que un viaje añadido se puede eliminar correctamente de la lista.

updateTravelItem modifies existing item
Asegura que si se actualiza un viaje (por ejemplo, cambiando el título), ese cambio se refleje correctamente en la lista.

addActivityToTravel adds activity correctly
Evalúa que al agregar una actividad a un viaje, esta se añada correctamente dentro del viaje correspondiente.

removeActivityFromTravel removes activity correctly
Comprueba que se puede eliminar correctamente una actividad específica de un viaje.

updateActivityInTravel updates an activity correctly
Valida que una actividad dentro de un viaje puede ser actualizada correctamente (por ejemplo, modificando la duración).

startEditing and stopEditing change editingItemId correctly
Asegura que al iniciar o finalizar la edición de un viaje, el estado editingItemId del ViewModel se actualice correctamente.


### Resultados de los tests unitarios

Se han ejecutado un total de 7 tests unitarios sobre el `TravelListViewModel`. Todos los tests se han completado satisfactoriamente, validando la lógica de gestión de datos de la lista de viajes y actividades.

| Test                                                   | Resultado   |
|--------------------------------------------------------|-------------|
| addTravelItem adds a new item                          | ✅ Correcto |
| deleteTravelItem removes the item                      | ✅ Correcto |
| updateTravelItem modifies existing item                | ✅ Correcto |
| addActivityToTravel adds activity correctly            | ✅ Correcto |
| removeActivityFromTravel removes activity correctly    | ✅ Correcto |
| updateActivityInTravel updates an activity correctly   | ✅ Correcto |
| startEditing and stopEditing change editingItemId      | ✅ Correcto |

