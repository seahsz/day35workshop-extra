import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { WeatherService } from '../../weather.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-home',
  standalone: false,
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit, OnDestroy {

  private fb = inject(FormBuilder);
  private weatherSvc = inject(WeatherService);
  protected citySub !: Subscription;

  protected form !: FormGroup;
  protected cities: string[] = []; // Just an array of City names

  ngOnInit(): void {
    this.form = this.createForm();
    this.citySub = this.weatherSvc.cities$.subscribe(
      cities => this.cities = cities
    )
  }

  submitForm() {
    const value = this.form.controls['city'].value;
    console.info('>>> Calling getWeather with: ', value);
    this.weatherSvc.getWeather(value).subscribe({}); // I do not need it to do anything yet
    this.form.reset();
  }

  deleteCity(city: string) {
    console.info('Deleting from localStorage: ', city)
    this.weatherSvc.deleteCity(city);
  }

  private createForm(): FormGroup {
    return this.fb.group({
      city: this.fb.control<string>('', [Validators.required, Validators.minLength(1)])
    })
  }

  ngOnDestroy(): void {
    this.citySub.unsubscribe();
  }

}
