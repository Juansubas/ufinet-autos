import { Routes } from '@angular/router';
import { Login} from './components/login/login';
import { Register } from './components/register/register';
import { CarList } from './components/car-list/car-list';
import { authGuard } from './guards/auth-guard';

export const routes: Routes = [
    { path: 'login', component: Login },
    { path: 'register', component: Register },
    {
        path: 'cars',
        component: CarList,
        canActivate: [authGuard] // Aplicamos el guardián para proteger esta ruta
    },
    { path: '', redirectTo: '/cars', pathMatch: 'full' },
    { path: '**', redirectTo: '/login' } // Redirige cualquier ruta no encontrada
];
