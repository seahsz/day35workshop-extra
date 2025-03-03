import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { WeatherService } from '../../weather.service';
import { City } from '../../models';
import { Observable } from 'rxjs';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-city',
  standalone: false,
  templateUrl: './city.component.html',
  styleUrl: './city.component.css'
})
export class CityComponent implements OnInit{

  private activatedRoute = inject(ActivatedRoute);
  private weatherSvc = inject(WeatherService);
  private titleSvc = inject(Title);

  protected cityName = "";
  protected city$ !: Observable<City>;

  ngOnInit(): void {
    this.activatedRoute.params.subscribe(
      params => {
        this.cityName = params['name'];
        this.titleSvc.setTitle(this.cityName);
        this.city$ = this.weatherSvc.getWeather(this.cityName);
      }
    )
  }
}
