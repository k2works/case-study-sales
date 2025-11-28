'use strict';

import fs from 'fs';
import path from 'path';
import { execSync } from 'child_process';

// Increase buffer to safely handle large diffs/logs
const MAX_BUFFER = 256 * 1024 * 1024; // 256 MB

// Function to register the journal:generate task
export default function(gulp) {
  // Helper function to generate journal for a specific date
  const generateJournalForDate = (dateStr, journalDir) => {
    // Format date as YYYYMMDD
    const dateObj = new Date(dateStr);
    const formattedDate = dateObj.toISOString().slice(0, 10).replace(/-/g, '');

    // File path for this date's journal
    const journalFile = path.join(journalDir, `${formattedDate}.md`);

    // Get commits for this date
    const commits = execSync(`git log --since="${dateStr} 00:00:00" --until="${dateStr} 23:59:59" --format="%h %s%n%b"`, { maxBuffer: MAX_BUFFER }).toString();

    // Skip if no commits
    if (!commits.trim()) {
      console.log(`No commits found for ${dateStr}`);
      return false;
    }

    // Get detailed changes for each commit on this date
    const commitHashesOutput = execSync(`git log --since="${dateStr} 00:00:00" --until="${dateStr} 23:59:59" --format="%h"`, { maxBuffer: MAX_BUFFER }).toString();
    const commitHashes = commitHashesOutput.split('\n').map(line => line.trim()).filter(Boolean);

    const detailedCommits = [];

    commitHashes.forEach(hash => {
      // Get commit details
      const commitMessage = execSync(`git show -s --format="%s%n%b" ${hash}`, { maxBuffer: MAX_BUFFER }).toString().trim();

      // Get files changed
      const filesChangedOutput = execSync(`git show --name-status ${hash}`, { maxBuffer: MAX_BUFFER }).toString();
      const filesChanged = filesChangedOutput.split('\n')
          .map(line => line.trim())
          .filter(line => /^[AMDRT]\s/.test(line));

      // Get code changes (diff) with robust handling for large diffs
      let diff = '';
      try {
        diff = execSync(`git show ${hash} --color=never`, { maxBuffer: MAX_BUFFER }).toString();
      } catch (e) {
        // Fallback to a summary if diff is too large or any error occurs
        const summary = execSync(`git show --stat --oneline ${hash} --color=never`, { maxBuffer: MAX_BUFFER }).toString();
        diff = `[Diff too large or failed to load. Showing summary instead.]\n\n${summary}`;
      }

      detailedCommits.push({
        hash,
        message: commitMessage,
        filesChanged,
        diff
      });
    });

    // Create journal content
    let content = `# 作業履歴 ${dateStr}\n\n`;
    content += `## 概要\n\n`;
    content += `${dateStr}の作業内容をまとめています。\n\n`;

    // Add each commit
    detailedCommits.forEach(commit => {
      content += `## コミット: ${commit.hash}\n\n`;
      content += `### メッセージ\n\n`;
      content += `\`\`\`\n${commit.message}\n\`\`\`\n\n`;

      content += `### 変更されたファイル\n\n`;
      commit.filesChanged.forEach(file => {
        content += `- ${file}\n`;
      });
      content += `\n`;

      content += `### 変更内容\n\n`;
      content += `\`\`\`diff\n${commit.diff}\n\`\`\`\n\n`;

      // Add PlantUML diagram placeholder if there are significant structural changes
      const hasStructuralChanges = commit.filesChanged.some(f =>
          f.endsWith('.rb') &&
          (f.includes('model') || f.includes('controller') || f.includes('service'))
      );

      if (hasStructuralChanges) {
        content += `### 構造変更\n\n`;
        content += `\`\`\`plantuml\n@startuml\n`;
        content += `' このコミットによる構造変更を表すダイアグラムをここに追加してください\n`;
        content += `' 例:\n`;
        content += `' class NewClass\n`;
        content += `' class ExistingClass\n`;
        content += `' NewClass --> ExistingClass\n`;
        content += `@enduml\n\`\`\`\n\n`;
      }
    });

    // Write to file
    fs.writeFileSync(journalFile, content);
    console.log(`Created journal entry for ${dateStr} at ${journalFile}`);
    return true;
  };

  // Journal generation task for a specific date
  gulp.task('journal:generate:date', (done) => {
    // Get date from command line arguments
    const args = process.argv.slice(3);
    const dateArg = args.find(arg => arg.startsWith('--date='));

    if (!dateArg) {
      console.error('Error: Please provide a date using --date=YYYY-MM-DD format');
      done();
      return;
    }

    const dateStr = dateArg.split('=')[1];

    // Validate date format
    if (!/^\d{4}-\d{2}-\d{2}$/.test(dateStr)) {
      console.error('Error: Date must be in YYYY-MM-DD format');
      done();
      return;
    }

    // Directory to store journal entries
    const journalDir = path.join('docs', 'journal');

    // Create directory if it doesn't exist
    if (!fs.existsSync(journalDir)) {
      fs.mkdirSync(journalDir, { recursive: true });
    }

    const result = generateJournalForDate(dateStr, journalDir);

    if (!result) {
      console.log(`No journal entry was created for ${dateStr}`);
    }

    done();
  });

  // Helper function to generate explanation for a commit based on its message and files
  const generateExplanation = (message, filesChanged) => {
    // Parse commit type from message
    const typeMatch = message.match(/^(fix|feat|refactor|chore|docs|test|style|perf|ci|build)[\(:]/i);
    const commitType = typeMatch ? typeMatch[1].toLowerCase() : 'other';

    // Determine affected layers based on file paths
    const layers = new Set();
    filesChanged.forEach(file => {
      if (file.includes('domain')) layers.add('ドメイン層');
      if (file.includes('service')) layers.add('サービス層');
      if (file.includes('controller') || file.includes('api')) layers.add('プレゼンテーション層');
      if (file.includes('repository') || file.includes('mapper') || file.includes('datasource')) layers.add('インフラストラクチャ層');
      if (file.includes('migration') || file.includes('.sql')) layers.add('データベース');
      if (file.includes('frontend') || file.includes('.tsx') || file.includes('.jsx')) layers.add('フロントエンド');
      if (file.includes('test')) layers.add('テスト');
      if (file.includes('docs')) layers.add('ドキュメント');
      if (file.includes('config') || file.includes('gradle') || file.includes('package.json')) layers.add('ビルド設定');
    });

    // Generate purpose based on commit type
    let purpose = '';
    switch (commitType) {
      case 'fix':
        purpose = 'バグ修正または不具合対応を行い、システムの安定性・信頼性を向上させる。';
        break;
      case 'feat':
        purpose = '新機能の実装により、システムの機能性を拡張する。';
        break;
      case 'refactor':
        purpose = 'コード構造の改善により、保守性・可読性を向上させる。';
        break;
      case 'chore':
        purpose = '開発環境・ビルド設定の整備により、開発効率を向上させる。';
        break;
      case 'docs':
        purpose = 'ドキュメントの更新により、プロジェクトの理解しやすさを向上させる。';
        break;
      case 'test':
        purpose = 'テストの追加・修正により、コード品質の保証を強化する。';
        break;
      case 'style':
        purpose = 'コードスタイルの統一により、コードベースの一貫性を維持する。';
        break;
      case 'perf':
        purpose = 'パフォーマンス改善により、システムの応答性・効率性を向上させる。';
        break;
      case 'ci':
        purpose = 'CI/CD パイプラインの改善により、デプロイメントプロセスを最適化する。';
        break;
      case 'build':
        purpose = 'ビルドシステムの改善により、ビルドプロセスの効率化・安定化を図る。';
        break;
      default:
        purpose = 'システムの改善・機能拡張を行う。';
    }

    // Generate technical background
    let background = '';
    switch (commitType) {
      case 'fix':
        background = '既存機能における問題点を特定し、適切な修正を実施。修正により、想定される使用シナリオでの正常動作を確保。';
        break;
      case 'feat':
        background = 'ユーザー要件または技術的要件に基づき、新機能を設計・実装。既存アーキテクチャとの整合性を保ちながら拡張。';
        break;
      case 'refactor':
        background = '既存コードの動作を変更せずに、内部構造を改善。SOLID 原則やクリーンアーキテクチャの考え方に基づくリファクタリング。';
        break;
      case 'chore':
        background = '開発ワークフローの効率化や依存関係の更新など、非機能的な改善を実施。';
        break;
      case 'docs':
        background = 'プロジェクトドキュメントの追加・更新により、知識の共有と将来の開発効率向上を図る。';
        break;
      case 'test':
        background = 'テスト駆動開発（TDD）または既存機能のテストカバレッジ向上のため、テストケースを実装。';
        break;
      default:
        background = 'プロジェクトの品質向上または機能拡張のための変更を実施。';
    }

    // Generate impact scope
    const layerList = layers.size > 0 ? Array.from(layers).join('、') : '特定のレイヤに限定されない';
    const impact = `影響を受けるレイヤ: ${layerList}。変更は関連するテストおよびドキュメントへの波及を考慮する必要がある。`;

    return {
      purpose,
      background,
      impact
    };
  };

  // Task to add explanations to existing journal files
  gulp.task('journal:add-explanations', (done) => {
    const journalDir = path.join('docs', 'journal');

    if (!fs.existsSync(journalDir)) {
      console.error('Error: Journal directory does not exist');
      done();
      return;
    }

    // Get all journal files
    const files = fs.readdirSync(journalDir).filter(f => f.endsWith('.md'));

    files.forEach(file => {
      const filePath = path.join(journalDir, file);
      let content = fs.readFileSync(filePath, 'utf-8');

      // Skip if already has explanations
      if (content.includes('### 作業解説')) {
        console.log(`Skipping ${file} - already has explanations`);
        return;
      }

      // Parse commits from the file
      const commitSections = content.split(/(?=## コミット:)/);
      let newContent = commitSections[0]; // Keep the header

      for (let i = 1; i < commitSections.length; i++) {
        let section = commitSections[i];

        // Extract message
        const messageMatch = section.match(/### メッセージ\n\n```\n([\s\S]*?)\n```/);
        const message = messageMatch ? messageMatch[1] : '';

        // Extract files changed
        const filesMatch = section.match(/### 変更されたファイル\n\n([\s\S]*?)(?=\n###|\n## |$)/);
        const filesText = filesMatch ? filesMatch[1] : '';
        const filesChanged = filesText.split('\n').filter(f => f.startsWith('- ')).map(f => f.substring(2));

        // Generate explanation
        const explanation = generateExplanation(message, filesChanged);

        // Insert explanation section before "変更内容"
        const insertPoint = section.indexOf('### 変更内容');
        if (insertPoint !== -1) {
          const explanationSection = `### 作業解説\n\n#### 目的\n${explanation.purpose}\n\n#### 技術的な背景\n${explanation.background}\n\n#### 変更の影響範囲\n${explanation.impact}\n\n`;
          section = section.slice(0, insertPoint) + explanationSection + section.slice(insertPoint);
        }

        newContent += section;
      }

      // Write updated content
      fs.writeFileSync(filePath, newContent);
      console.log(`Updated ${file} with explanations`);
    });

    console.log('Explanation addition completed.');
    done();
  });

  // Journal generation task for all dates
  gulp.task('journal:generate', (done) => {
    // Directory to store journal entries
    const journalDir = path.join('docs', 'journal');

    // Create directory if it doesn't exist
    if (!fs.existsSync(journalDir)) {
      fs.mkdirSync(journalDir, { recursive: true });
    }

    // Get all commit dates
    const datesOutput = execSync('git log --format=%ad --date=short', { maxBuffer: MAX_BUFFER }).toString();
    const dates = [...new Set(datesOutput.split('\n').map(line => line.trim()).filter(Boolean))];

    dates.forEach(dateStr => {
      // Format date as YYYYMMDD
      const dateObj = new Date(dateStr);
      const formattedDate = dateObj.toISOString().slice(0, 10).replace(/-/g, '');

      // File path for this date's journal
      const journalFile = path.join(journalDir, `${formattedDate}.md`);

      // Skip if file already exists
      if (fs.existsSync(journalFile)) {
        return;
      }

      // Generate journal for this date
      generateJournalForDate(dateStr, journalDir);
    });

    console.log("Journal generation completed.");
    done();
  });
}
