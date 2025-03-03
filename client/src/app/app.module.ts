import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { provideHttpClient } from '@angular/common/http';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { CityComponent } from './components/city/city.component';
import { WeatherService } from './weather.service';
import { NotificationService } from './notification.service';

import { ReactiveFormsModule } from '@angular/forms';

import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';

const appRoutes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'city/:name', component: CityComponent }
]

const materialModules = [
  MatSnackBarModule, MatFormFieldModule, MatInputModule,
  MatButtonModule
]

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    CityComponent
  ],
  imports: [
    BrowserModule, RouterModule.forRoot(appRoutes),
    materialModules, ReactiveFormsModule
  ],
  providers: [provideHttpClient(), 
    WeatherService, NotificationService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
