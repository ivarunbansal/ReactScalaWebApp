import config from "config";

export function getStates(){
    return fetch(config.apiEndPoint+"/states")
    .then(response=>{
        return response.json();
    })
}

export function getStateById(id){
    return fetch(config.apiEndPoint+"/states/state?id="+id)
    .then(response=>{
        return response.json();
    })
}





export function updateState(updatedState,headers=undefined){
    return fetch(config.apiEndPoint+"/states/state?id="+updatedState.id,
    {
        method: 'PUT',
        headers: Object.assign({
                    'Content-Type': 'application/json'
                 }, headers),
        body: JSON.stringify(updatedState)
    }
    )
 //   .then(response=>{
   //     return response.json();
   // })
   
  
}

export function createState(state,headers=undefined){
    return fetch(config.apiEndPoint+"/states/state",
    {
        method: 'POST',
        headers: Object.assign({
                    'Content-Type': 'application/json'
                 }, headers),
        body: JSON.stringify(state)
    }
    )
    // .then(response=>{
    //     console.log("response is ", response);
    //     retu
    // })
   
  
}
