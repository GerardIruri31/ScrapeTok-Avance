import apify_client
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
import asyncio
from typing import Dict, Any

app = FastAPI()
# Habilitar CORS en FastAPI
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

async def fetch_data(run_input):
    try:
        client = apify_client.ApifyClient(run_input["apifyToken"])
        run_input.pop("apifyToken",None)
        loop = asyncio.get_event_loop()
        run = await loop.run_in_executor(None, lambda: client.actor("clockworks/free-tiktok-scraper").call(run_input=run_input))
        dataset_id = run.get("defaultDatasetId")
        if not dataset_id:
            return {"onError": {"error": "datasetId not found on the Apify response"}}
        data = await loop.run_in_executor(None, lambda: list(client.dataset(dataset_id).iterate_items()))
        return {"Success": data}
    except Exception as ApifyApiError:
        print("Error: " + str(ApifyApiError))
        # Pasa cuando no hay ningún post que hagan match con filtros envíados 
        return {"Error": str(ApifyApiError)}
    

@app.post("/APIFYCALL")
async def fetch_tiktok_data(request: Dict[str,Any]):
    return await fetch_data(request)