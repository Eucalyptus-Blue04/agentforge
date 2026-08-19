        // ========== 简约 SVG 图标 ==========
        const ICONS = {
            logo: '<svg viewBox="0 0 24 24" class="icon logo-icon"><path d="M12 2a4 4 0 0 1 4 4v2a4 4 0 0 1-8 0V6a4 4 0 0 1 4-4z"/><path d="M8 10v1a4 4 0 0 0 8 0v-1"/><line x1="12" y1="17" x2="12" y2="21"/><circle cx="9" cy="7" r="0.5" fill="currentColor" stroke="none"/><circle cx="15" cy="7" r="0.5" fill="currentColor" stroke="none"/></svg>',
            chat: '<svg viewBox="0 0 24 24" class="icon"><path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/></svg>',
            agent: '<svg viewBox="0 0 24 24" class="icon"><rect x="3" y="11" width="18" height="10" rx="2"/><circle cx="9" cy="16" r="1.5" fill="currentColor" stroke="none"/><circle cx="15" cy="16" r="1.5" fill="currentColor" stroke="none"/><path d="M12 2v6"/><circle cx="12" cy="2" r="1"/></svg>',
            wrench: '<svg viewBox="0 0 24 24" class="icon"><path d="M14.7 6.3a1 1 0 0 0 0 1.4l1.6 1.6a1 1 0 0 0 1.4 0l3.77-3.77a6 6 0 0 1-7.94 7.94l-6.91 6.91a2.12 2.12 0 0 1-3-3l6.91-6.91a6 6 0 0 1 7.94-7.94l-3.76 3.76z"/></svg>',
            bolt: '<svg viewBox="0 0 24 24" class="icon"><polygon points="13 2 3 14 12 14 11 22 21 10 12 10 13 2"/></svg>',
            brain: '<svg viewBox="0 0 24 24" class="icon"><path d="M9.5 2a3.5 3.5 0 0 0-3.2 2.1A3.5 3.5 0 0 0 3 7.5C3 9.4 4.1 11 5.7 11.7A3.5 3.5 0 0 0 5 14.5 3.5 3.5 0 0 0 8.5 18H9"/><path d="M14.5 2a3.5 3.5 0 0 1 3.2 2.1A3.5 3.5 0 0 1 21 7.5c0 1.9-1.1 3.5-2.7 4.2.7.8 1.2 2 1.2 3.3a3.5 3.5 0 0 1-3.5 3.5H15"/><path d="M12 2v16"/></svg>',
            plus: '<svg viewBox="0 0 24 24" class="icon"><line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/></svg>',
            search: '<svg viewBox="0 0 24 24" class="icon"><circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/></svg>',
            chart: '<svg viewBox="0 0 24 24" class="icon"><line x1="18" y1="20" x2="18" y2="10"/><line x1="12" y1="20" x2="12" y2="4"/><line x1="6" y1="20" x2="6" y2="14"/></svg>',
            edit: '<svg viewBox="0 0 24 24" class="icon"><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/><path d="M18.5 2.5a2.12 2.12 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/></svg>',
            clipboard: '<svg viewBox="0 0 24 24" class="icon"><path d="M16 4h2a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2h2"/><rect x="8" y="2" width="8" height="4" rx="1" ry="1"/></svg>',
            user: '<svg viewBox="0 0 24 24" class="icon"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>',
            error: '<svg viewBox="0 0 24 24" class="icon"><circle cx="12" cy="12" r="10"/><line x1="15" y1="9" x2="9" y2="15"/><line x1="9" y1="9" x2="15" y2="15"/></svg>',
            check: '<svg viewBox="0 0 24 24" class="icon"><polyline points="20 6 9 17 4 12"/></svg>',
            copy: '<svg viewBox="0 0 24 24" class="icon"><rect x="9" y="9" width="13" height="13" rx="2" ry="2"/><path d="M5 15H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h9a2 2 0 0 1 2 2v1"/></svg>',
            refresh: '<svg viewBox="0 0 24 24" class="icon"><polyline points="23 4 23 10 17 10"/><path d="M20.49 15a9 9 0 1 1-2.12-9.36L23 10"/></svg>',
            globe: '<svg viewBox="0 0 24 24" class="icon"><circle cx="12" cy="12" r="10"/><line x1="2" y1="12" x2="22" y2="12"/><path d="M12 2a15.3 15.3 0 0 1 4 10 15.3 15.3 0 0 1-4 10 15.3 15.3 0 0 1-4-10 15.3 15.3 0 0 1 4-10z"/></svg>',
            doc: '<svg viewBox="0 0 24 24" class="icon"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/><polyline points="14 2 14 8 20 8"/><line x1="16" y1="13" x2="8" y2="13"/><line x1="16" y1="17" x2="8" y2="17"/><polyline points="10 9 9 9 8 9"/></svg>',
            arrow: '<svg viewBox="0 0 24 24" class="icon icon-sm"><polyline points="9 18 15 12 9 6"/></svg>',
            trash: '<svg viewBox="0 0 24 24" class="icon"><polyline points="3 6 5 6 21 6"/><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"/></svg>',
        };

        function icon(name, cls) {
            return '<span class="' + (cls || 'icon') + '">' + (ICONS[name] || '') + '</span>';
        }
        // ========== 页面加载 ==========
        window.addEventListener('load', () => {
            setTimeout(() => {
                document.getElementById('pageLoader').classList.add('hidden');
            }, 500);
        });

        // ========== 主题切换 ==========
        const MOON_SVG = '<svg viewBox="0 0 24 24" class="icon"><path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"/></svg>';
        const SUN_SVG = '<svg viewBox="0 0 24 24" class="icon"><circle cx="12" cy="12" r="5"/><line x1="12" y1="1" x2="12" y2="3"/><line x1="12" y1="21" x2="12" y2="23"/><line x1="4.22" y1="4.22" x2="5.64" y2="5.64"/><line x1="18.36" y1="18.36" x2="19.78" y2="19.78"/><line x1="1" y1="12" x2="3" y2="12"/><line x1="21" y1="12" x2="23" y2="12"/><line x1="4.22" y1="19.78" x2="5.64" y2="18.36"/><line x1="18.36" y1="5.64" x2="19.78" y2="4.22"/></svg>';

        function toggleTheme() {
            const html = document.documentElement;
            const icon = document.getElementById('themeIcon');
            const current = html.getAttribute('data-theme');
            const next = current === 'dark' ? 'light' : 'dark';
            html.setAttribute('data-theme', next);
            localStorage.setItem('theme', next);
            icon.style.transform = next === 'dark' ? 'rotate(0deg)' : 'rotate(180deg)';
            icon.innerHTML = next === 'dark' ? MOON_SVG : SUN_SVG;
        }

        (function() {
            const saved = localStorage.getItem('theme') || 'light';
            document.documentElement.setAttribute('data-theme', saved);
            document.getElementById('themeIcon').innerHTML = saved === 'dark' ? MOON_SVG : SUN_SVG;
        })();

        // ========== 导航切换 ==========
        function showPanel(name, el) {
            document.querySelectorAll('.panel').forEach(p => p.classList.remove('active'));
            document.querySelectorAll('.nav-item').forEach(n => n.classList.remove('active'));
            document.getElementById('panel-' + name).classList.add('active');
            if (el) el.classList.add('active');

            if (name === 'agents') loadAgents();
            if (name === 'tools') loadTools();
            if (name === 'memory') loadMemory();
        }

        // ========== 发送消息 ==========
        // (sendMessage 已在下方会话管理部分重写，支持文件上传和 sessionId)

        function addMessage(content, type, meta) {
            const box = document.getElementById('chatBox');
            const div = document.createElement('div');
            div.className = 'message ' + (type === 'user' ? 'user' : 'agent');

            const USER_AVATAR = '<svg viewBox="0 0 24 24" class="icon"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>';
            const AGENT_AVATAR = '<svg viewBox="0 0 24 24" class="icon"><rect x="3" y="11" width="18" height="10" rx="2"/><circle cx="9" cy="16" r="1.5" fill="currentColor" stroke="none"/><circle cx="15" cy="16" r="1.5" fill="currentColor" stroke="none"/><path d="M12 2v6"/><circle cx="12" cy="2" r="1"/></svg>';
            const ERROR_AVATAR = '<svg viewBox="0 0 24 24" class="icon"><circle cx="12" cy="12" r="10"/><line x1="15" y1="9" x2="9" y2="15"/><line x1="9" y1="9" x2="15" y2="15"/></svg>';
            const avatar = type === 'user' ? USER_AVATAR : type === 'error' ? ERROR_AVATAR : AGENT_AVATAR;
            const avatarClass = type === 'user' ? 'user' : 'agent';

            // agent 消息渲染 markdown，其他原样显示
            const rendered = (type === 'agent') ? markdownToHtml(content) : escapeHtml(content);

            div.innerHTML = `
                <div class="msg-avatar ${avatarClass}">${avatar}</div>
                <div class="msg-content">
                    <div class="msg-bubble">${rendered}</div>
                    ${type === 'agent' ? `
                    <div class="msg-footer">
                        <button class="icon-btn" onclick="copyMessage(this)" title="复制"><svg viewBox="0 0 24 24" class="icon icon-sm"><rect x="9" y="9" width="13" height="13" rx="2" ry="2"/><path d="M5 15H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h9a2 2 0 0 1 2 2v1"/></svg></button>
                        <button class="icon-btn" onclick="regenerateMessage()" title="重新生成"><svg viewBox="0 0 24 24" class="icon icon-sm"><polyline points="23 4 23 10 17 10"/><path d="M20.49 15a9 9 0 1 1-2.12-9.36L23 10"/></svg></button>
                    </div>` : ''}
                </div>
            `;
            if (type === 'agent') {
                div.querySelector('.icon-btn').dataset.raw = div.querySelector('.msg-bubble').textContent;
            }
            box.appendChild(div);
            scrollChat();
        }

        function scrollChat() {
            const box = document.getElementById('chatBox');
            box.scrollTop = box.scrollHeight;
        }

        function clearChat() {
            // 清除服务端会话
            fetch('/api/chat/sessions/' + currentSessionId, { method: 'DELETE' }).catch(() => {});
            resetChatBox();
        }

        function copyMessage(btn) {
            const COPY_SVG = '<svg viewBox="0 0 24 24" class="icon icon-sm"><rect x="9" y="9" width="13" height="13" rx="2" ry="2"/><path d="M5 15H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h9a2 2 0 0 1 2 2v1"/></svg>';
            const CHECK_SVG = '<svg viewBox="0 0 24 24" class="icon icon-sm"><polyline points="20 6 9 17 4 12"/></svg>';
            const raw = btn.dataset.raw || btn.closest('.msg-content').querySelector('.msg-bubble').textContent;
            navigator.clipboard.writeText(raw).then(() => {
                btn.innerHTML = CHECK_SVG;
                setTimeout(() => btn.innerHTML = COPY_SVG, 1500);
            }).catch(() => {
                const ta = document.createElement('textarea');
                ta.value = raw;
                document.body.appendChild(ta);
                ta.select();
                document.execCommand('copy');
                document.body.removeChild(ta);
                btn.innerHTML = CHECK_SVG;
                setTimeout(() => btn.innerHTML = COPY_SVG, 1500);
            });
        }

        function regenerateMessage() {
            // 找到最后一条用户消息
            const messages = document.querySelectorAll('#chatBox .message.user');
            if (messages.length === 0) return;
            const lastUserMsg = messages[messages.length - 1];
            const content = lastUserMsg.querySelector('.msg-bubble').textContent;

            // 删除最后一条 agent 回复
            const allMessages = document.querySelectorAll('#chatBox .message');
            const lastMsg = allMessages[allMessages.length - 1];
            if (lastMsg && lastMsg.classList.contains('agent')) {
                lastMsg.remove();
            }

            // 清除服务端会话中的最后一条消息，重新发送
            document.getElementById('userInput').value = content;
            sendMessage(true);
        }

        function escapeHtml(s) {
            return s.replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;');
        }

        function markdownToHtml(md) {
            if (!md) return '';
            let html = escapeHtml(md);

            // 代码块 ```...```
            html = html.replace(/```(\w*)\n([\s\S]*?)```/g, '<pre><code class="lang-$1">$2</code></pre>');

            // 行内代码 `...`
            html = html.replace(/`([^`]+)`/g, '<code>$1</code>');

            // 粗体 **...**
            html = html.replace(/\*\*(.+?)\*\*/g, '<strong>$1</strong>');

            // 斜体 *...*
            html = html.replace(/(?<!\*)\*(?!\*)(.+?)(?<!\*)\*(?!\*)/g, '<em>$1</em>');

            // 删除线 ~~...~~
            html = html.replace(/~~(.+?)~~/g, '<del>$1</del>');

            // 标题 # ... ## ... ###
            html = html.replace(/^### (.+)$/gm, '<h4>$1</h4>');
            html = html.replace(/^## (.+)$/gm, '<h3>$1</h3>');
            html = html.replace(/^# (.+)$/gm, '<h2>$1</h2>');

            // 无序列表
            html = html.replace(/^[\-\*] (.+)$/gm, '<li>$1</li>');
            html = html.replace(/(<li>.*<\/li>\n?)+/g, '<ul>$&</ul>');

            // 有序列表
            html = html.replace(/^\d+\. (.+)$/gm, '<li>$1</li>');

            // 链接 [text](url)
            html = html.replace(/\[([^\]]+)\]\(([^)]+)\)/g, '<a href="$2" target="_blank" rel="noopener">$1</a>');

            // 换行
            html = html.replace(/\n/g, '<br>');

            return html;
        }

        // ========== 加载 Agent ==========
        async function loadAgents() {
            try {
                const resp = await fetch('/api/agents');
                const text = await resp.text();
                if (!text || text.trimStart().startsWith('<')) { document.getElementById('agentGrid').innerHTML = '<p style="color:var(--error)">加载失败</p>'; return; }
                const agents = JSON.parse(text);
                const grid = document.getElementById('agentGrid');
                grid.innerHTML = agents.map(a => `
                    <div class="agent-card glass-subtle glass-hover glass-glow">
                        <div class="agent-card-header">
                            <div class="agent-card-icon"><svg viewBox="0 0 24 24" class="icon icon-lg"><rect x="3" y="11" width="18" height="10" rx="2"/><circle cx="9" cy="16" r="1.5" fill="currentColor" stroke="none"/><circle cx="15" cy="16" r="1.5" fill="currentColor" stroke="none"/><path d="M12 2v6"/><circle cx="12" cy="2" r="1"/></svg></div>
                            <h3>${a.name}</h3>
                        </div>
                        <p>${a.description || '无描述'}</p>
                        <div class="agent-card-meta">
                            <span>模型: ${a.modelName}</span>
                            <span>温度: ${a.temperature}</span>
                        </div>
                        <div class="agent-tools">
                            ${(a.tools || []).map(t => '<span class="tool-tag">' + t + '</span>').join('')}
                            ${(!a.tools || a.tools.length === 0) ? '<span style="color:var(--text-muted);font-size:11px">无工具</span>' : ''}
                        </div>
                    </div>
                `).join('');
            } catch (e) {
                document.getElementById('agentGrid').innerHTML = '<p style="color:var(--error)">加载失败: ' + e.message + '</p>';
            }
        }

        // ========== 加载工具 ==========
        async function loadTools() {
            const tools = [
                { name: 'calculate', desc: '数学计算', params: 'expression: 数学表达式' },
                { name: 'http_get', desc: 'HTTP GET 请求', params: 'url: 请求地址' },
                { name: 'http_post', desc: 'HTTP POST 请求', params: 'url, body' },
                { name: 'read_file', desc: '读取文件', params: 'filePath: 文件路径' },
                { name: 'list_directory', desc: '列出目录', params: 'dirPath: 目录路径' },
                { name: 'web_search', desc: '搜索互联网', params: 'query: 搜索关键词' },
                { name: 'baike_search', desc: '搜索百度百科', params: 'query: 搜索关键词' },
                { name: 'get_current_time', desc: '获取当前时间', params: 'timezone: 时区 (可选)' },
                { name: 'format_datetime', desc: '格式化时间', params: 'datetime, format' },
                { name: 'calculate_duration', desc: '计算时长', params: 'start, end' },
                { name: 'parse_json', desc: '解析 JSON', params: 'json: JSON 字符串' },
                { name: 'extract_json_field', desc: '提取 JSON 字段', params: 'json, fieldPath' },
                { name: 'create_json', desc: '创建 JSON', params: 'keyValuePairs: 键值对' },
                { name: 'validate_json', desc: '验证 JSON', params: 'json: JSON 字符串' },
                { name: 'string_replace', desc: '字符串替换', params: 'text, target, replacement' },
                { name: 'regex_match', desc: '正则匹配', params: 'text, regex' },
                { name: 'string_split', desc: '字符串分割', params: 'text, delimiter' },
                { name: 'string_length', desc: '字符串长度', params: 'text' },
                { name: 'string_trim', desc: '去除空白', params: 'text' },
                { name: 'string_contains', desc: '包含检查', params: 'text, search' },
            ];

            document.getElementById('toolTable').innerHTML = tools.map(t => `
                <tr>
                    <td><span class="tool-name">${t.name}</span></td>
                    <td>${t.desc}</td>
                    <td style="color:var(--text-muted);font-size:12px">${t.params}</td>
                </tr>
            `).join('');
        }

        // ========== 加载记忆 ==========
        async function loadMemory() {
            const agent = document.getElementById('memoryAgentSelect').value;
            try {
                const resp = await fetch('/api/agents/' + agent + '/memory?count=20');
                const text = await resp.text();
                if (!text || text.trimStart().startsWith('<')) { document.getElementById('memoryContent').innerHTML = '<p style="color:var(--error)">加载失败</p>'; return; }
                const data = JSON.parse(text);
                const content = document.getElementById('memoryContent');

                if (data.memory === '暂无记忆') {
                    content.innerHTML = `
                        <div class="empty-state">
                            <div class="empty-state-icon"><svg viewBox="0 0 24 24" class="icon icon-lg"><path d="M9.5 2a3.5 3.5 0 0 0-3.2 2.1A3.5 3.5 0 0 0 3 7.5C3 9.4 4.1 11 5.7 11.7A3.5 3.5 0 0 0 5 14.5 3.5 3.5 0 0 0 8.5 18H9"/><path d="M14.5 2a3.5 3.5 0 0 1 3.2 2.1A3.5 3.5 0 0 1 21 7.5c0 1.9-1.1 3.5-2.7 4.2.7.8 1.2 2 1.2 3.3a3.5 3.5 0 0 1-3.5 3.5H15"/><path d="M12 2v16"/></svg></div>
                            <h3>暂无记忆</h3>
                            <p>请先与该 Agent 对话</p>
                        </div>
                    `;
                    return;
                }

                const lines = data.memory.split('\n').filter(l => l.trim());
                content.innerHTML = '<div class="memory-list">' + lines.map(line => {
                    let type = 'agent';
                    if (line.startsWith('[用户]')) type = 'user';
                    else if (line.startsWith('[工具')) type = 'tool';
                    return '<div class="memory-item glass-subtle"><span class="memory-type ' + type + '">' + type + '</span><div class="memory-content">' + escapeHtml(line) + '</div></div>';
                }).join('') + '</div>';
            } catch (e) {
                document.getElementById('memoryContent').innerHTML = '<p style="color:var(--error)">加载失败</p>';
            }
        }

        async function clearMemory() {
            const agent = document.getElementById('memoryAgentSelect').value;
            await fetch('/api/agents/' + agent + '/memory', { method: 'DELETE' });
            loadMemory();
        }

        // ========== 创建 Agent ==========
        async function createAgent() {
            const name = document.getElementById('newAgentName').value.trim();
            if (!name) { alert('请输入 Agent 名称'); return; }

            const toolsSelect = document.getElementById('newAgentTools');
            const tools = Array.from(toolsSelect.selectedOptions).map(o => o.value);

            const body = {
                name,
                description: document.getElementById('newAgentDesc').value,
                systemPrompt: document.getElementById('newAgentPrompt').value,
                temperature: parseFloat(document.getElementById('newAgentTemp').value),
                maxTokens: parseInt(document.getElementById('newAgentTokens').value),
                tools
            };

            try {
                const resp = await fetch('/api/agents', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(body)
                });
                const text = await resp.text();
                if (!text || text.trimStart().startsWith('<')) { document.getElementById('createResult').innerHTML = '<p style="color:var(--error)">✕ 服务器返回非 JSON 响应</p>'; return; }
                const data = JSON.parse(text);
                document.getElementById('createResult').innerHTML = '<p style="color:var(--success)">✓ Agent "' + data.name + '" 创建成功！</p>';

                const opt = document.createElement('option');
                opt.value = data.name;
                opt.textContent = data.name;
                document.getElementById('agentSelect').appendChild(opt);
            } catch (e) {
                document.getElementById('createResult').innerHTML = '<p style="color:var(--error)">✕ 创建失败: ' + e.message + '</p>';
            }
        }

        // ========== 事件日志 ==========
        const events = [];
        function addEvent(type, agent, data) {
            const now = new Date().toLocaleTimeString();
            events.unshift({ time: now, type, agent, data });
            if (events.length > 50) events.pop();
            renderEvents();
        }

        function renderEvents() {
            const log = document.getElementById('eventLog');
            if (events.length === 0) {
                log.innerHTML = `
                    <div class="empty-state">
                        <div class="empty-state-icon"><svg viewBox="0 0 24 24" class="icon icon-lg"><circle cx="12" cy="12" r="10"/><line x1="2" y1="12" x2="22" y2="12"/><path d="M12 2a15.3 15.3 0 0 1 4 10 15.3 15.3 0 0 1-4 10 15.3 15.3 0 0 1-4-10 15.3 15.3 0 0 1 4-10z"/></svg></div>
                        <h3>等待事件...</h3>
                        <p>事件将在这里实时显示</p>
                    </div>
                `;
                return;
            }
            log.innerHTML = events.map(e => `
                <div class="event-item">
                    <span class="event-time">${e.time}</span>
                    <span class="event-type ${e.type}">${e.type}</span>
                    <span class="event-data">[${e.agent}] ${e.data || ''}</span>
                </div>
            `).join('');
        }

        function clearEvents() {
            events.length = 0;
            renderEvents();
        }

        // ========== WebSocket ==========
        function connectWebSocket() {
            const ws = new WebSocket('ws://localhost:8080/ws/agents');
            ws.onmessage = (e) => {
                try {
                    const data = JSON.parse(e.data);
                    addEvent(data.type, data.agentName, JSON.stringify(data.data));
                } catch {}
            };
            ws.onclose = () => setTimeout(connectWebSocket, 3000);
            ws.onerror = () => {};
        }

        // ========== 会话管理 ==========
        let currentSessionId = generateId();
        let sessions = [{ id: currentSessionId, title: '新对话', time: new Date() }];

        function generateId() {
            return 'sess_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9);
        }

        // 从后端加载会话列表
        async function loadSessions() {
            try {
                const resp = await fetch('/api/chat/sessions');
                if (!resp.ok) return;
                const data = await resp.json();
                if (data.length > 0) {
                    sessions = data.map(s => ({
                        id: s.sessionId,
                        title: s.title || '新对话',
                        time: new Date(s.updatedAt),
                        agentName: s.agentName
                    }));
                    currentSessionId = sessions[0].id;
                    await restoreSessionMessages(currentSessionId);
                    renderSessionList();
                }
            } catch (e) {
                console.warn('Failed to load sessions:', e);
            }
        }

        function newConversation() {
            // 创建新会话
            currentSessionId = generateId();
            sessions.unshift({ id: currentSessionId, title: '新对话', time: new Date() });

            // 清空聊天区域
            resetChatBox();
            renderSessionList();

            // 关闭会话列表
            document.getElementById('sessionList').classList.remove('show');
        }

        async function switchSession(sessionId) {
            if (sessionId === currentSessionId) return;

            // 切换会话
            currentSessionId = sessionId;
            const session = sessions.find(s => s.id === sessionId);
            if (session) {
                session.time = new Date();
            }

            // 从后端加载消息
            await restoreSessionMessages(sessionId);
            renderSessionList();

            // 关闭会话列表
            document.getElementById('sessionList').classList.remove('show');
        }

        // 从后端加载会话消息
        async function restoreSessionMessages(sessionId) {
            try {
                const resp = await fetch('/api/chat/sessions/' + sessionId + '/messages');
                if (!resp.ok) {
                    resetChatBox();
                    return;
                }
                const messages = await resp.json();
                if (messages.length === 0) {
                    resetChatBox();
                    return;
                }

                const chatBox = document.getElementById('chatBox');
                const USER_AVATAR = '<svg viewBox="0 0 24 24" class="icon"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>';
                const AGENT_AVATAR = '<svg viewBox="0 0 24 24" class="icon"><rect x="3" y="11" width="18" height="10" rx="2"/><circle cx="9" cy="16" r="1.5" fill="currentColor" stroke="none"/><circle cx="15" cy="16" r="1.5" fill="currentColor" stroke="none"/><path d="M12 2v6"/><circle cx="12" cy="2" r="1"/></svg>';

                chatBox.innerHTML = messages.map(msg => {
                    const isUser = msg.role === 'user';
                    const avatar = isUser ? USER_AVATAR : AGENT_AVATAR;
                    const cls = isUser ? 'user' : 'agent';
                    const rendered = isUser ? escapeHtml(msg.content) : markdownToHtml(msg.content);
                    return `<div class="message ${cls}">
                        <div class="msg-avatar ${cls}">${avatar}</div>
                        <div class="msg-content">
                            <div class="msg-bubble">${rendered}</div>
                        </div>
                    </div>`;
                }).join('');

                scrollChat();
            } catch (e) {
                console.warn('Failed to load messages:', e);
                resetChatBox();
            }
        }

        function resetChatBox() {
            document.getElementById('chatBox').innerHTML = `
                <div class="message agent">
                    <div class="msg-avatar agent"><svg viewBox="0 0 24 24" class="icon"><rect x="3" y="11" width="18" height="10" rx="2"/><circle cx="9" cy="16" r="1.5" fill="currentColor" stroke="none"/><circle cx="15" cy="16" r="1.5" fill="currentColor" stroke="none"/><path d="M12 2v6"/><circle cx="12" cy="2" r="1"/></svg></div>
                    <div class="msg-content">
                        <div class="msg-bubble">你好！我是 AgentForge AI 助手。\n\n有什么可以帮你的？</div>
                        <div class="msg-meta">Agent · 刚刚</div>
                    </div>
                </div>`;
        }

        function toggleSessionList() {
            const list = document.getElementById('sessionList');
            list.classList.toggle('show');
            if (list.classList.contains('show')) {
                renderSessionList();
            }
        }

        function renderSessionList() {
            const list = document.getElementById('sessionList');
            list.innerHTML = sessions.map(s => `
                <div class="session-item ${s.id === currentSessionId ? 'active' : ''}" onclick="switchSession('${s.id}')">
                    <span>💬</span>
                    <span class="session-title">${escapeHtml(s.title)}</span>
                    ${sessions.length > 1 ? `<span class="session-delete" onclick="event.stopPropagation();deleteSession('${s.id}')" title="删除">✕</span>` : ''}
                </div>
            `).join('');
        }

        async function deleteSession(sessionId) {
            if (sessions.length <= 1) return;
            sessions = sessions.filter(s => s.id !== sessionId);

            // 从后端删除
            try {
                await fetch('/api/chat/sessions/' + sessionId, { method: 'DELETE' });
            } catch (e) {}

            if (sessionId === currentSessionId) {
                currentSessionId = sessions[0].id;
                await restoreSessionMessages(currentSessionId);
            }
            renderSessionList();
        }

        // 点击外部关闭会话列表
        document.addEventListener('click', (e) => {
            const wrapper = document.querySelector('.session-wrapper');
            if (wrapper && !wrapper.contains(e.target)) {
                document.getElementById('sessionList').classList.remove('show');
            }
        });

        // ========== 文件上传 ==========
        let pendingFiles = [];

        function onFileSelected(input) {
            const files = Array.from(input.files);
            if (files.length === 0) return;

            files.forEach(file => {
                if (file.size > 10 * 1024 * 1024) {
                    addMessage('文件 "' + file.name + '" 超过 10MB 限制', 'error');
                    return;
                }
                pendingFiles.push(file);
            });

            renderFilePreview();
            input.value = ''; // 清空 input 允许重复选择同一文件
        }

        function getFileIcon(file) {
            const name = file.name.toLowerCase();
            if (file.type.startsWith('image/')) return '🖼️';
            if (name.endsWith('.pdf')) return '📕';
            if (name.match(/\.docx?$/)) return '📝';
            if (name.match(/\.xlsx?$/)) return '📊';
            if (name.match(/\.pptx?$/)) return '📽️';
            if (name.match(/\.(py|js|java|html|css|json|xml|yaml|yml)$/i)) return '💻';
            return '📄';
        }

        function renderFilePreview() {
            const bar = document.getElementById('filePreviewBar');
            if (pendingFiles.length === 0) {
                bar.classList.remove('show');
                bar.innerHTML = '';
                return;
            }

            bar.classList.add('show');
            bar.innerHTML = pendingFiles.map((file, index) => {
                const isImage = file.type.startsWith('image/');
                const isPdf = file.name.toLowerCase().endsWith('.pdf');
                const isDoc = file.name.match(/\.docx?$/i);
                const isExcel = file.name.match(/\.xlsx?$/i);
                const isPpt = file.name.match(/\.pptx?$/i);
                const icon = getFileIcon(file);
                const size = formatFileSize(file.size);
                let hint = '';
                if (isPdf) hint = '提取PDF文本';
                else if (isDoc) hint = '提取Word内容';
                else if (isExcel) hint = '提取表格数据';
                else if (isPpt) hint = '提取幻灯片内容';
                else if (isImage) hint = 'OCR文字识别';

                if (isImage) {
                    return `<div class="file-preview-item">
                        <img src="${URL.createObjectURL(file)}" alt="${escapeHtml(file.name)}">
                        <span class="file-name" title="${escapeHtml(file.name)}">${escapeHtml(file.name)}</span>
                        <span style="color:var(--text-muted);font-size:11px">${size}</span>
                        <span class="file-remove" onclick="removeFile(${index})">✕</span>
                    </div>`;
                }
                return `<div class="file-preview-item">
                    <span>${icon}</span>
                    <span class="file-name" title="${escapeHtml(file.name)}${hint ? '\n' + hint : ''}">${escapeHtml(file.name)}</span>
                    <span style="color:var(--text-muted);font-size:11px">${size}</span>
                    <span class="file-remove" onclick="removeFile(${index})">✕</span>
                </div>`;
            }).join('');
        }

        function removeFile(index) {
            pendingFiles.splice(index, 1);
            renderFilePreview();
        }

        function formatFileSize(bytes) {
            if (bytes < 1024) return bytes + 'B';
            if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + 'KB';
            return (bytes / (1024 * 1024)).toFixed(1) + 'MB';
        }

        // ========== 语音对话模式 ==========
        const voiceConv = (() => {
            // 状态常量
            const IDLE = 'idle';
            const LISTENING = 'listening';
            const PROCESSING = 'processing';
            const SPEAKING = 'speaking';

            // DOM 引用
            const overlay = document.getElementById('voiceOverlay');
            const centerIcon = document.getElementById('voiceCenterIcon');
            const statusLabel = document.getElementById('voiceStatusLabel');
            const transcriptBox = document.getElementById('voiceTranscript');
            const replyBox = document.getElementById('voiceReply');

            // 状态
            let state = IDLE;
            let recognition = null;
            let speechRate = 1.0;
            let currentUtterance = null;
            let isActive = false;
            let silenceTimer = null;
            let finalTranscript = '';
            let interimTranscript = '';
            let currentReplyText = '';

            // TTS 分句器：将长文本拆分为可朗读的片段
            function splitForTTS(text) {
                if (!text || text.trim().length === 0) return [];
                // 清理 markdown 语法
                let clean = text
                    .replace(/```[\s\S]*?```/g, '代码块已省略')
                    .replace(/`([^`]+)`/g, '$1')
                    .replace(/\*\*(.+?)\*\*/g, '$1')
                    .replace(/\*(.+?)\*/g, '$1')
                    .replace(/~~(.+?)~~/g, '$1')
                    .replace(/#{1,6}\s+/g, '')
                    .replace(/\[([^\]]+)\]\([^)]+\)/g, '$1')
                    .replace(/^[>\-\*]\s+/gm, '')
                    .replace(/^\d+\.\s+/gm, '');

                // 按句子分割（中文句号、问号、感叹号、英文句号、换行）
                const sentences = clean.split(/(?<=[。！？.!?\n])\s*/);
                const chunks = [];
                let current = '';

                for (const s of sentences) {
                    const trimmed = s.trim();
                    if (!trimmed) continue;
                    if (current.length + trimmed.length > 200 && current.length > 0) {
                        chunks.push(current.trim());
                        current = trimmed;
                    } else {
                        current += (current ? ' ' : '') + trimmed;
                    }
                }
                if (current.trim()) chunks.push(current.trim());
                return chunks.filter(c => c.length > 0);
            }

            // TTS 播放
            function speak(text) {
                return new Promise((resolve) => {
                    if (!('speechSynthesis' in window)) {
                        resolve();
                        return;
                    }
                    // 取消之前的播放
                    window.speechSynthesis.cancel();

                    const chunks = splitForTTS(text);
                    if (chunks.length === 0) {
                        resolve();
                        return;
                    }

                    setState(SPEAKING);
                    let index = 0;

                    function speakNext() {
                        if (index >= chunks.length || !isActive) {
                            resolve();
                            return;
                        }
                        const utterance = new SpeechSynthesisUtterance(chunks[index]);
                        utterance.lang = 'zh-CN';
                        utterance.rate = speechRate;
                        utterance.pitch = 1.0;

                        // 尝试选择中文语音
                        const voices = window.speechSynthesis.getVoices();
                        const zhVoice = voices.find(v =>
                            v.lang.startsWith('zh') && (v.name.includes('Xiaoxiao') || v.name.includes('Xiaoyi') || v.name.includes('Hanhan') || v.name.includes('Chinese') || v.name.includes('中文'))
                        ) || voices.find(v => v.lang.startsWith('zh'));
                        if (zhVoice) utterance.voice = zhVoice;

                        utterance.onend = () => {
                            index++;
                            speakNext();
                        };
                        utterance.onerror = (e) => {
                            if (e.error !== 'interrupted') {
                                index++;
                                speakNext();
                            }
                        };
                        currentUtterance = utterance;
                        window.speechSynthesis.speak(utterance);
                    }

                    speakNext();
                });
            }

            // 停止 TTS
            function stopSpeaking() {
                if ('speechSynthesis' in window) {
                    window.speechSynthesis.cancel();
                }
                currentUtterance = null;
            }

            // 更新 UI 状态
            function setState(newState) {
                state = newState;
                overlay.className = 'voice-overlay show ' + newState;

                const icons = { idle: '🎤', listening: '🔴', processing: '⏳', speaking: '🔊' };
                const labels = { idle: '准备就绪', listening: '正在聆听...', processing: 'AI 思考中...', speaking: 'AI 正在回答...' };
                centerIcon.textContent = icons[newState] || '🎤';
                statusLabel.textContent = labels[newState] || '';
            }

            // 开始语音识别
            function startRecognition() {
                if (!('webkitSpeechRecognition' in window) && !('SpeechRecognition' in window)) {
                    statusLabel.textContent = '浏览器不支持语音识别';
                    return;
                }

                const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
                recognition = new SpeechRecognition();
                recognition.lang = 'zh-CN';
                recognition.continuous = true;
                recognition.interimResults = true;
                recognition.maxAlternatives = 1;

                finalTranscript = '';
                interimTranscript = '';

                recognition.onstart = () => {
                    if (!isActive) return;
                    setState(LISTENING);
                    transcriptBox.textContent = '';
                };

                recognition.onresult = (event) => {
                    if (!isActive) return;
                    interimTranscript = '';
                    for (let i = event.resultIndex; i < event.results.length; i++) {
                        const t = event.results[i][0].transcript;
                        if (event.results[i].isFinal) {
                            finalTranscript += t;
                        } else {
                            interimTranscript += t;
                        }
                    }
                    transcriptBox.textContent = finalTranscript + interimTranscript;

                    // 重置静音计时器
                    clearTimeout(silenceTimer);
                    if (finalTranscript.trim()) {
                        silenceTimer = setTimeout(() => {
                            if (state === LISTENING && isActive && finalTranscript.trim()) {
                                onUserFinished(finalTranscript.trim());
                            }
                        }, 2000);
                    }
                };

                recognition.onerror = (event) => {
                    if (!isActive) return;
                    if (event.error === 'no-speech') {
                        // 没检测到语音，继续监听
                        return;
                    }
                    if (event.error === 'not-allowed') {
                        statusLabel.textContent = '麦克风权限被拒绝，请在浏览器设置中允许';
                        setTimeout(() => close(), 3000);
                        return;
                    }
                    console.warn('Voice recognition error:', event.error);
                };

                recognition.onend = () => {
                    if (!isActive) return;
                    // 如果还在监听状态，自动重启识别
                    if (state === LISTENING) {
                        try {
                            recognition.start();
                        } catch (e) {
                            // 忽略重复启动错误
                        }
                    }
                };

                try {
                    recognition.start();
                } catch (e) {
                    statusLabel.textContent = '语音识别启动失败: ' + e.message;
                }
            }

            // 停止语音识别
            function stopRecognition() {
                if (recognition) {
                    recognition.stop();
                    recognition = null;
                }
                clearTimeout(silenceTimer);
            }

            // 用户说完一句话，发送到 AI
            async function onUserFinished(text) {
                if (!isActive) return;
                stopRecognition();
                setState(PROCESSING);
                replyBox.textContent = '';
                replyBox.classList.remove('show');
                currentReplyText = '';

                try {
                    const agentName = document.getElementById('agentSelect').value;
                    const resp = await fetch('/api/chat/stream', {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({ message: text, sessionId: currentSessionId, agentName })
                    });

                    if (!resp.ok || !resp.body) {
                        // 降级到非流式
                        const fallback = await fetch('/api/chat', {
                            method: 'POST',
                            headers: { 'Content-Type': 'application/json' },
                            body: JSON.stringify({ message: text, sessionId: currentSessionId, agentName })
                        });
                        const data = await fallback.json();
                        if (data.error) {
                            statusLabel.textContent = '错误: ' + (data.message || '请求失败');
                            setTimeout(() => { if (isActive) startRecognition(); }, 2000);
                            return;
                        }
                        currentReplyText = data.content || '';
                        if (currentReplyText) {
                            replyBox.textContent = currentReplyText;
                            replyBox.classList.add('show');
                            await speak(currentReplyText);
                        }
                        if (isActive) startRecognition();
                        return;
                    }

                    // 流式读取
                    const reader = resp.body.getReader();
                    const decoder = new TextDecoder();
                    let buffer = '';
                    let done = false;

                    while (!done && isActive) {
                        const { done: streamDone, value } = await reader.read();
                        if (streamDone) break;

                        buffer += decoder.decode(value, { stream: true });
                        const lines = buffer.split('\n');
                        buffer = lines.pop();

                        for (const line of lines) {
                            if (!line.startsWith('data:')) continue;
                            const jsonStr = line.substring(5).trim();
                            if (!jsonStr) continue;

                            try {
                                const data = JSON.parse(jsonStr);
                                if (data.error) {
                                    statusLabel.textContent = '错误: ' + (data.message || '请求失败');
                                    done = true;
                                    break;
                                }
                                if (data.delta) {
                                    currentReplyText += data.delta;
                                    replyBox.textContent = currentReplyText;
                                    replyBox.classList.add('show');
                                }
                                if (data.done) {
                                    done = true;
                                    break;
                                }
                            } catch (e) {}
                        }
                    }

                    // AI 回复完成，播放语音
                    if (isActive && currentReplyText) {
                        await speak(currentReplyText);
                    }

                    // 播放完毕，继续监听
                    if (isActive) startRecognition();

                } catch (e) {
                    if (isActive) {
                        statusLabel.textContent = '请求失败: ' + e.message;
                        setTimeout(() => { if (isActive) startRecognition(); }, 2000);
                    }
                }
            }

            // 启动语音对话
            function start() {
                isActive = true;
                finalTranscript = '';
                interimTranscript = '';
                currentReplyText = '';
                transcriptBox.textContent = '';
                replyBox.textContent = '';
                replyBox.classList.remove('show');
                overlay.classList.add('show');
                document.getElementById('voiceModeBtn').classList.add('active');
                startRecognition();
            }

            // 停止对话
            function stop() {
                isActive = false;
                stopRecognition();
                stopSpeaking();
                setState(IDLE);
                transcriptBox.textContent = '';
                replyBox.textContent = '';
                replyBox.classList.remove('show');
            }

            // 关闭语音模式
            function close() {
                stop();
                overlay.classList.remove('show');
                overlay.className = 'voice-overlay';
                document.getElementById('voiceModeBtn').classList.remove('active');
            }

            // 设置语速
            function setRate(rate) {
                speechRate = rate;
                document.querySelectorAll('.voice-speed-btn').forEach(btn => {
                    btn.classList.toggle('active', parseFloat(btn.dataset.rate) === rate);
                });
            }

            // 预加载语音列表
            if ('speechSynthesis' in window) {
                window.speechSynthesis.getVoices();
                window.speechSynthesis.onvoiceschanged = () => {
                    window.speechSynthesis.getVoices();
                };
            }

            return { start, stop, close, setRate };
        })();

        // ========== 发送消息（支持文件上传和 sessionId） ==========
        async function sendMessage(skipUserBubble) {
            const input = document.getElementById('userInput');
            const message = input.value.trim();
            const hasFiles = pendingFiles.length > 0;

            if (!message && !hasFiles) return;

            // 显示用户消息气泡
            if (!skipUserBubble) {
                let attachmentHtml = '';
                if (hasFiles) {
                    const parts = pendingFiles.map(f => {
                        const icon = getFileIcon(f);
                        const isImage = f.type.startsWith('image/');
                        if (isImage) {
                            // 图片：内联显示缩略图
                            return `<div class="msg-attachment" style="flex-direction:column;align-items:flex-start;gap:6px">
                                <div style="display:flex;align-items:center;gap:8px"><span class="attachment-icon">${icon}</span><span class="attachment-name">${escapeHtml(f.name)}</span></div>
                                <img src="${URL.createObjectURL(f)}" style="max-width:200px;max-height:150px;border-radius:8px;object-fit:cover">
                            </div>`;
                        }
                        return `<div class="msg-attachment"><span class="attachment-icon">${icon}</span><span class="attachment-name">${escapeHtml(f.name)}</span></div>`;
                    });
                    attachmentHtml = parts.join('');
                }
                addMessageWithAttachment(message || '(文件)', 'user', attachmentHtml);
            }

            input.value = '';
            const filesToSend = [...pendingFiles];
            pendingFiles = [];
            renderFilePreview();

            const sendBtn = document.getElementById('sendBtn');
            sendBtn.disabled = true;
            sendBtn.textContent = '思考中...';

            // 显示输入中动画
            const typingDiv = document.createElement('div');
            typingDiv.className = 'message agent';
            typingDiv.innerHTML = '<div class="msg-avatar agent"><svg viewBox="0 0 24 24" class="icon"><rect x="3" y="11" width="18" height="10" rx="2"/><circle cx="9" cy="16" r="1.5" fill="currentColor" stroke="none"/><circle cx="15" cy="16" r="1.5" fill="currentColor" stroke="none"/><path d="M12 2v6"/><circle cx="12" cy="2" r="1"/></svg></div><div class="msg-content"><div class="msg-bubble"><div class="typing"><span></span><span></span><span></span></div></div></div>';
            document.getElementById('chatBox').appendChild(typingDiv);
            scrollChat();

            try {
                if (hasFiles) {
                    // 有文件：使用 FormData 上传
                    await sendWithFile(message, filesToSend, typingDiv);
                } else {
                    // 无文件：使用原来的流式接口，但带上 sessionId
                    await sendStreamOnly(message, typingDiv);
                }
            } catch (e) {
                typingDiv.remove();
                addMessage('发送失败: ' + e.message, 'error');
            }

            sendBtn.disabled = false;
            sendBtn.textContent = '发送';
        }

        // 纯文本流式发送（带 sessionId）
        async function sendStreamOnly(message, typingDiv) {
            try {
                const agentName = document.getElementById('agentSelect').value;
                const resp = await fetch('/api/chat/stream', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ message, sessionId: currentSessionId, agentName })
                });

                if (!resp.ok || !resp.body) {
                    await fallbackChatWithSession(message, typingDiv);
                    return;
                }

                const reader = resp.body.getReader();
                const decoder = new TextDecoder();
                let buffer = '';
                let fullContent = '';
                let bubbleDiv = null;
                let finished = false;

                while (true) {
                    const { done, value } = await reader.read();
                    if (done) break;

                    buffer += decoder.decode(value, { stream: true });
                    const lines = buffer.split('\n');
                    buffer = lines.pop();

                    for (const line of lines) {
                        if (!line.startsWith('data:')) continue;
                        const jsonStr = line.substring(5).trim();
                        if (!jsonStr) continue;

                        try {
                            const data = JSON.parse(jsonStr);

                            if (data.error) {
                                typingDiv.remove();
                                addMessage('错误: ' + (data.message || '请求失败'), 'error');
                                finished = true;
                                break;
                            }

                            if (data.delta) {
                                fullContent += data.delta;
                                if (!bubbleDiv) {
                                    typingDiv.remove();
                                    const box = document.getElementById('chatBox');
                                    const msgDiv = document.createElement('div');
                                    msgDiv.className = 'message agent';
                                    msgDiv.innerHTML = '<div class="msg-avatar agent"><svg viewBox="0 0 24 24" class="icon"><rect x="3" y="11" width="18" height="10" rx="2"/><circle cx="9" cy="16" r="1.5" fill="currentColor" stroke="none"/><circle cx="15" cy="16" r="1.5" fill="currentColor" stroke="none"/><path d="M12 2v6"/><circle cx="12" cy="2" r="1"/></svg></div><div class="msg-content"><div class="msg-bubble"></div><div class="msg-footer"><button class="icon-btn" onclick="copyMessage(this)" title="复制"><svg viewBox="0 0 24 24" class="icon icon-sm"><rect x="9" y="9" width="13" height="13" rx="2" ry="2"/><path d="M5 15H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h9a2 2 0 0 1 2 2v1"/></svg></button><button class="icon-btn" onclick="regenerateMessage()" title="重新生成"><svg viewBox="0 0 24 24" class="icon icon-sm"><polyline points="23 4 23 10 17 10"/><path d="M20.49 15a9 9 0 1 1-2.12-9.36L23 10"/></svg></button></div></div>';
                                    box.appendChild(msgDiv);
                                    bubbleDiv = msgDiv.querySelector('.msg-bubble');
                                    msgDiv.querySelector('.icon-btn').dataset.raw = '';
                                }
                                bubbleDiv.innerHTML = markdownToHtml(fullContent);
                                const msgContent = bubbleDiv.closest('.msg-content');
                                if (msgContent) {
                                    const copyBtn = msgContent.querySelector('.icon-btn');
                                    if (copyBtn) copyBtn.dataset.raw = fullContent;
                                }
                                scrollChat();
                            }

                            if (data.done) {
                                finished = true;
                                if (!bubbleDiv) {
                                    typingDiv.remove();
                                    addMessage(data.content || fullContent, 'agent');
                                }
                                addEvent('AGENT_COMPLETED', 'chat', (data.content || fullContent || '').substring(0, 80) + '...');
                            }
                        } catch (parseErr) {}
                    }
                    if (finished) break;
                }

                if (!finished && !bubbleDiv) {
                    typingDiv.remove();
                    if (fullContent) {
                        addMessage(fullContent, 'agent');
                    } else {
                        await fallbackChatWithSession(message, null);
                    }
                } else if (!finished && bubbleDiv) {
                    addEvent('AGENT_COMPLETED', 'chat', fullContent.substring(0, 80) + '...');
                }
            } catch (e) {
                await fallbackChatWithSession(message, typingDiv);
            }
        }

        // 带文件的流式发送
        async function sendWithFile(message, files, typingDiv) {
            try {
                const agentName = document.getElementById('agentSelect').value;
                const formData = new FormData();
                formData.append('message', message || '');
                formData.append('sessionId', currentSessionId);
                formData.append('agentName', agentName);
                files.forEach(file => formData.append('file', file));

                const resp = await fetch('/api/chat/stream-with-file', {
                    method: 'POST',
                    body: formData
                });

                if (!resp.ok || !resp.body) {
                    typingDiv.remove();
                    addMessage('文件上传失败', 'error');
                    return;
                }

                const reader = resp.body.getReader();
                const decoder = new TextDecoder();
                let buffer = '';
                let fullContent = '';
                let bubbleDiv = null;
                let finished = false;

                while (true) {
                    const { done, value } = await reader.read();
                    if (done) break;

                    buffer += decoder.decode(value, { stream: true });
                    const lines = buffer.split('\n');
                    buffer = lines.pop();

                    for (const line of lines) {
                        if (!line.startsWith('data:')) continue;
                        const jsonStr = line.substring(5).trim();
                        if (!jsonStr) continue;

                        try {
                            const data = JSON.parse(jsonStr);

                            if (data.error) {
                                typingDiv.remove();
                                addMessage('错误: ' + (data.message || '请求失败'), 'error');
                                finished = true;
                                break;
                            }

                            if (data.delta) {
                                fullContent += data.delta;
                                if (!bubbleDiv) {
                                    typingDiv.remove();
                                    addStreamingBubble(fullContent);
                                    bubbleDiv = document.querySelector('#chatBox .message:last-child .msg-bubble');
                                }
                                bubbleDiv.innerHTML = markdownToHtml(fullContent);
                                const msgContent = bubbleDiv.closest('.msg-content');
                                if (msgContent) {
                                    const copyBtn = msgContent.querySelector('.icon-btn');
                                    if (copyBtn) copyBtn.dataset.raw = fullContent;
                                }
                                scrollChat();
                            }

                            if (data.done) {
                                finished = true;
                                if (!bubbleDiv) {
                                    typingDiv.remove();
                                    addMessage(data.content || fullContent, 'agent');
                                }
                            }
                        } catch (parseErr) {}
                    }
                    if (finished) break;
                }

                if (!finished && !bubbleDiv) {
                    typingDiv.remove();
                    if (fullContent) {
                        addMessage(fullContent, 'agent');
                    }
                }
            } catch (e) {
                typingDiv.remove();
                addMessage('文件上传失败: ' + e.message, 'error');
            }
        }

        function addStreamingBubble(initialContent) {
            const box = document.getElementById('chatBox');
            const msgDiv = document.createElement('div');
            msgDiv.className = 'message agent';
            msgDiv.innerHTML = '<div class="msg-avatar agent"><svg viewBox="0 0 24 24" class="icon"><rect x="3" y="11" width="18" height="10" rx="2"/><circle cx="9" cy="16" r="1.5" fill="currentColor" stroke="none"/><circle cx="15" cy="16" r="1.5" fill="currentColor" stroke="none"/><path d="M12 2v6"/><circle cx="12" cy="2" r="1"/></svg></div><div class="msg-content"><div class="msg-bubble"></div><div class="msg-footer"><button class="icon-btn" onclick="copyMessage(this)" title="复制"><svg viewBox="0 0 24 24" class="icon icon-sm"><rect x="9" y="9" width="13" height="13" rx="2" ry="2"/><path d="M5 15H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h9a2 2 0 0 1 2 2v1"/></svg></button><button class="icon-btn" onclick="regenerateMessage()" title="重新生成"><svg viewBox="0 0 24 24" class="icon icon-sm"><polyline points="23 4 23 10 17 10"/><path d="M20.49 15a9 9 0 1 1-2.12-9.36L23 10"/></svg></button></div></div>';
            box.appendChild(msgDiv);
            const bubble = msgDiv.querySelector('.msg-bubble');
            bubble.innerHTML = markdownToHtml(initialContent);
            const copyBtn = msgDiv.querySelector('.icon-btn');
            if (copyBtn) copyBtn.dataset.raw = initialContent;
        }

        function addMessageWithAttachment(content, type, attachmentHtml) {
            const box = document.getElementById('chatBox');
            const div = document.createElement('div');
            div.className = 'message ' + (type === 'user' ? 'user' : 'agent');

            const USER_AVATAR = '<svg viewBox="0 0 24 24" class="icon"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>';
            const avatar = type === 'user' ? USER_AVATAR : '';
            const avatarClass = type === 'user' ? 'user' : 'agent';
            const rendered = escapeHtml(content);

            div.innerHTML = `
                <div class="msg-avatar ${avatarClass}">${avatar}</div>
                <div class="msg-content">
                    <div class="msg-bubble">${rendered}</div>
                    ${attachmentHtml || ''}
                </div>
            `;
            box.appendChild(div);
            scrollChat();
        }

        // 降级接口（带 sessionId）
        async function fallbackChatWithSession(message, typingDiv) {
            try {
                const agentName = document.getElementById('agentSelect').value;
                const resp = await fetch('/api/chat', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ message, sessionId: currentSessionId, agentName })
                });
                if (typingDiv) typingDiv.remove();

                const text = await resp.text();
                if (!text) { addMessage('错误: 服务器无响应', 'error'); return; }

                const data = JSON.parse(text);
                if (data.error) {
                    addMessage('错误: ' + (data.message || '请求失败'), 'error');
                } else {
                    addMessage(data.content, 'agent', data.durationMs + 'ms');
                }
            } catch (e) {
                if (typingDiv) typingDiv.remove();
                addMessage('网络错误: ' + e.message, 'error');
            }
        }

        // ========== 初始化 ==========
        loadAgents();
        loadSessions();
        connectWebSocket();
