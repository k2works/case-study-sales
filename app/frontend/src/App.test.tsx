import React from 'react';
import '@testing-library/jest-dom'
import {render, screen} from '@testing-library/react';
import {MemoryRouter} from 'react-router-dom';
import App from './App.tsx';

test('renders login page', () => {
    render(
        <MemoryRouter initialEntries={['/']}>
            <App/>
        </MemoryRouter>
    );

    expect(screen.getByText('SMS')).toBeInTheDocument();
});
