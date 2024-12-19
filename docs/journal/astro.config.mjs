// @ts-check
import { defineConfig } from 'astro/config';
import mdx from '@astrojs/mdx';
import expressiveCode from "astro-expressive-code";
import remarkMermaid from 'remark-mermaidjs'

import relativeLinks from 'astro-relative-links';

// https://astro.build/config
export default defineConfig({
    site: 'https://example.com',
    markdown: {
        // Applied to .md and .mdx files
        remarkPlugins: [remarkMermaid],
    },
    integrations: [expressiveCode(), mdx(), relativeLinks()]
});