import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { StorageService } from '../services/storage.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const storageService = inject(StorageService)
  const jwt = storageService.getToken();

  req = req.clone({
    headers: req.headers.set('Authorization', 'Bearer ' + jwt)
  })

  return next(req);
};
