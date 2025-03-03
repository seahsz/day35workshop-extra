import { HttpClient, HttpParams } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { BehaviorSubject, catchError, Observable, tap, throwError } from "rxjs";
import { City } from "./models";
import { NotificationService } from "./notification.service";

@Injectable()
export class WeatherService {

    private http = inject(HttpClient);
    private notificationSvc = inject(NotificationService);

    private citiesSubject = new BehaviorSubject<string[]>(this.loadCitiesFromStorage());
    cities$ = this.citiesSubject.asObservable(); // Public observable for components to subscribe to

    private loadCitiesFromStorage(): string[] {
        const storedCities = localStorage.getItem('savedCities');
        console.info("retrieving from storage: ", storedCities);
        return storedCities ? JSON.parse(storedCities) : [];
    }

    private saveCitiesToStorage(cities: string[]) {
        localStorage.setItem('savedCities', JSON.stringify(cities));
        this.citiesSubject.next(cities);
    }

    getWeather(city: string): Observable<City> {
        city = city.toLowerCase(); // toLowerCase
        const params = new HttpParams()
            .set('city', city);
        return this.http.get<City>('/api/search', { params }).pipe(
            tap(() => {
                const currCities = this.citiesSubject.value;
                console.info('>>> Number of current cities in local storage: ', currCities.length);
                if (!currCities.includes(city)) {
                    console.info('>>> Adding city: ', city);
                    const updatedCities = [city, ...currCities];
                    this.saveCitiesToStorage(updatedCities);
                }
            }),
            catchError(error => {
                console.error('>>> Error while calling API');
                if (error.status === 404) {
                    this.notificationSvc.showError(`${city} not found`);
                    throw new Error(`${city} not found`)
                }
                return throwError(() => error);
            })
        )
    }

    deleteCity(city: string) {
        const currCities = this.citiesSubject.value;
        const updatedCities = currCities.filter(c => c !== city);
        this.saveCitiesToStorage(updatedCities);
    }

}