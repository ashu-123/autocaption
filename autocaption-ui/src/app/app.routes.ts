import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { CaptionsGeneratorComponent } from './pages/captions-generator/captions-generator.component';

export const routes: Routes = [
    {
        path: 'oauth2/callback/google', component: LoginComponent
    },
    {
        path: 'captions/generate', component: CaptionsGeneratorComponent
    },
    { path: '', redirectTo: 'oauth2/callback/google', pathMatch: 'full' },
    { path: '**', redirectTo: 'oauth2/callback/google' } // fallback
];
